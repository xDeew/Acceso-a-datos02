package com.andrelut.gimnasioMVC.gui;

import com.andrelut.gimnasioMVC.enums.TipoSuscripcion;

import java.io.*;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;
    private boolean esNuevaBaseDeDatos = false;

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    private Connection conexion;

    public Modelo() {
        getPropValues();
    }

    public void conectar() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/", user, password)) {
            if (!baseDeDatosExiste(conn, "gimnasiodb")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE GimnasioDB");
                    System.out.println("Base de datos 'GimnasioDB' creada automáticamente.");
                    esNuevaBaseDeDatos = true; // solo si es primera vez entrará y se define true

                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        // volvemos a establecer conexion porque no se especifica la base de datos en la url, en esta nueva conexion si se especifica
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/gimnasiodb", user, password);
            System.out.println("Conectado a la base de datos 'GimnasioDB'.");
            if (esNuevaBaseDeDatos) { // si es nueva base de datos (true), se ejecuta el script sql para crear las tablas
                ejecutarScriptSQL(conexion, "src/bdgimnasio.sql");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean baseDeDatosExiste(Connection conn, String nombreBD) {
        try (ResultSet resultSet = conn.getMetaData().getCatalogs()) {
            while (resultSet.next()) {
                if (nombreBD.toLowerCase().equals(resultSet.getString(1).toLowerCase())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al comprobar la existencia de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean estaConectado() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al desconectar la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void ejecutarScriptSQL(Connection conn, String rutaScriptSQL) {
        File archivoSQL = new File(rutaScriptSQL);
        if (!archivoSQL.exists()) {
            System.out.println("El archivo de script SQL no existe: " + rutaScriptSQL);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoSQL));
             Statement stmt = conn.createStatement()) {

            String line;
            StringBuilder sb = new StringBuilder();
            boolean esBloqueFuncion = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }

                // Comenzar o finalizar un bloque de función/procedimiento
                if (line.startsWith("delimiter")) {
                    esBloqueFuncion = true; // Cambia el estado si se encuentra 'delimiter'
                    continue;
                }

                if (esBloqueFuncion || line.endsWith(";")) {
                    sb.append(line);
                    if (!esBloqueFuncion) {
                        // Ejecutar declaración SQL
                        try {
                            stmt.execute(sb.toString());
                            sb = new StringBuilder();
                        } catch (SQLException e) {
                            System.out.println("Error al ejecutar el script SQL: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    sb.append(line);
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error al ejecutar el script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "src/config.properties";
            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");
        } catch (IOException e) {
            System.out.println("Excepcion " + e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setPropValues(String ip, String user, String pass, String adminPassword) {
        OutputStream output = null;
        try {
            Properties prop = new Properties();

            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPassword);

            output = new FileOutputStream("src/config.properties");
            prop.store(output, null);

            this.ip = ip;
            this.user = user;
            this.password = pass;
            this.adminPassword = adminPassword;
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> obtenerNombresClientes() {
        List<String> nombresClientes = new ArrayList<>();
        String sentenciaSql = "SELECT nombre, apellido FROM Clientes";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sentenciaSql)) {

            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                nombresClientes.add(nombreCompleto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener nombres de los clientes: " + e.getMessage());
            e.printStackTrace();
        }

        return nombresClientes;
    }

    // Métodos para operar con la tabla Clientes
    public void insertarCliente(String nombre, String apellido, LocalDate fechaNacimiento, String email, String telefono, String direccion) {
        String sentenciaSql = "INSERT INTO Clientes (nombre, apellido, fecha_nacimiento, email, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(sentenciaSql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setDate(3, Date.valueOf(fechaNacimiento));
            pstmt.setString(4, email);
            pstmt.setString(5, telefono);
            pstmt.setString(6, direccion);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente insertado con éxito.");
            } else {
                System.out.println("No se pudo insertar el cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Object> obtenerDatosCliente(String nombreCompleto) {
        String[] partes = nombreCompleto.split(" ");
        String nombre = partes[0];
        String apellido;
        if (partes.length > 1) apellido = partes[1];
        else apellido = "";
        String sentenciaSql = "SELECT * FROM Clientes WHERE nombre = ? AND apellido = ?";
        Map<String, Object> datosCliente = new HashMap<>();

        try (PreparedStatement pstmt = conexion.prepareStatement(sentenciaSql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    datosCliente.put("nombre", rs.getString("nombre"));
                    datosCliente.put("apellido", rs.getString("apellido"));
                    datosCliente.put("email", rs.getString("email"));
                    datosCliente.put("telefono", rs.getString("telefono"));
                    datosCliente.put("fecha_nacimiento", rs.getDate("fecha_nacimiento").toLocalDate());
                    datosCliente.put("direccion", rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener datos del cliente: " + e.getMessage());
            e.printStackTrace();
        }

        return datosCliente;
    }

    public void insertarSuscripcion(LocalDate fechaInicio, LocalDate fechaFin, boolean pagado, String tipoSuscripcion, double precio, int idCliente) {
        String sentenciaSql = "INSERT INTO Suscripciones (fecha_inicio, fecha_fin, estado_pago, tipo, precio, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conexion.prepareStatement(sentenciaSql)) {
            pstmt.setDate(1, Date.valueOf(fechaInicio));
            pstmt.setDate(2, Date.valueOf(fechaFin));
            pstmt.setBoolean(3, pagado);
            pstmt.setString(4, tipoSuscripcion);
            pstmt.setDouble(5, precio);
            pstmt.setInt(6, idCliente);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Suscripción insertada con éxito.");
            } else {
                System.out.println("No se pudo insertar la suscripción.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la suscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double obtenerPrecioPorTipoSuscripcion(String tipoSuscripcion) {
        double precio = 0.0;
        switch (tipoSuscripcion) {
            case "Básica":
                precio = TipoSuscripcion.BASICA.getPrecio();
                break;
            case "Premium":
                precio = TipoSuscripcion.PREMIUM.getPrecio();
                break;
            case "Familiar":
                precio = TipoSuscripcion.FAMILIAR.getPrecio();
                break;
            case "Estudiante":
                precio = TipoSuscripcion.ESTUDIANTE.getPrecio();
                break;
            default:
                break;
        }
        return precio;
    }

    public int obtenerIdClientePorNombre(String nombreCompleto) {
        nombreCompleto = nombreCompleto.trim();
        int ultimoEspacio = nombreCompleto.lastIndexOf(" ");
        String nombre, apellido;
        if (ultimoEspacio != -1) {
            nombre = nombreCompleto.substring(0, ultimoEspacio);
            apellido = nombreCompleto.substring(ultimoEspacio + 1);
        } else {
            nombre = nombreCompleto;
            apellido = "";
        }

        String consultaSQL = "SELECT id_cliente FROM Clientes WHERE nombre = ? AND apellido = ? LIMIT 1";

        try (PreparedStatement pstmt = conexion.prepareStatement(consultaSQL)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cliente");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el ID del cliente: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public boolean tieneSuscripcionActiva(int idCliente) {
        LocalDate hoy = LocalDate.now();
        String consultaSQL = "SELECT COUNT(*) FROM Suscripciones WHERE id_cliente = ? AND fecha_inicio <= ? AND fecha_fin >= ?";

        try (PreparedStatement pstmt = conexion.prepareStatement(consultaSQL)) {
            pstmt.setInt(1, idCliente);
            pstmt.setDate(2, Date.valueOf(hoy));
            pstmt.setDate(3, Date.valueOf(hoy));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Verifica si el conteo es mayor que cero
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar suscripción activa: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public ResultSet consultarClientes() {
        String consultaSQL = "SELECT * FROM Clientes";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(consultaSQL);
            return rs;
        } catch (SQLException e) {
            System.out.println("Error al consultar clientes: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> obtenerTodosLosClientesYSusSuscripciones() {
        List<Map<String, Object>> datosClientes = new ArrayList<>();
        String consultaSQL = "SELECT c.id_cliente, s.tipo, s.precio, s.estado_pago FROM Clientes c JOIN Suscripciones s ON c.id_cliente = s.id_cliente";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            while (rs.next()) { // iteramos sobre cada fila en el ResultSet, devuelve true si hay una fila siguiente
                Map<String, Object> datosCliente = new HashMap<>();
                datosCliente.put("id_cliente", rs.getInt("id_cliente"));
                datosCliente.put("tipo", rs.getString("tipo"));
                datosCliente.put("precio", rs.getDouble("precio"));
                datosCliente.put("estado_pago", rs.getBoolean("estado_pago"));
                datosClientes.add(datosCliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes y sus suscripciones: " + e.getMessage());
            e.printStackTrace();
        }

        return datosClientes;
    }

    public double obtenerGananciasPorClienteAdd(int numClientes, String tipoSuscripcion) {
        double ganancias = 0.0;
        String sql = "{ ? = call ganancias_por_cliente_add(?, ?) }";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.registerOutParameter(1, Types.DOUBLE); // registramos el parametro de retorno
            stmt.setInt(2, numClientes);
            stmt.setString(3, tipoSuscripcion);
            stmt.execute(); // despues de ejecutar la funcion, el valor de retorno queda en el primer parametro
            ganancias = stmt.getDouble(1); // obtenemos el valor de retorno
        } catch (SQLException e) {
            System.out.println("Error al obtener las ganancias por cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return ganancias;
    }

    public boolean existeEmail(String email) {
        String consultaSQL = "SELECT COUNT(*) FROM Clientes WHERE email = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(consultaSQL)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                    // comprueba si hay un email existente, es decir, si ya existe una fila con ese email pues el conteo será mayor que cero
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia del email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet consultarSuscripciones() {
        String consultaSQL = "SELECT * FROM Suscripciones";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(consultaSQL);
            return rs;
        } catch (SQLException e) {
            System.out.println("Error al consultar suscripciones: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}