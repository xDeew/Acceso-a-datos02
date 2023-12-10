package com.andrelut.gimnasioMVC.gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

    private Connection conexion;

    public Modelo() {
        getPropValues();
        conectar();
    }

    public void conectar() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/", user, password)) {
            // Revisa si la base de datos 'GimnasioDB' ya existe.
            if (baseDeDatosExiste(conn, "GimnasioDB")) {
                // Si no existe, crea la base de datos y configura su estructura.
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE GimnasioDB");
                    System.out.println("Base de datos 'GimnasioDB' creada automáticamente.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("database exists")) {
                        System.out.println("La base de datos 'GimnasioDB' ya existe. Continuando con la ejecución del programa.");
                    } else {
                        e.printStackTrace();
                    }
                }
                // Conectar a la base de datos recién creada para configurar su estructura.
                try (Connection connGimnasio = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/GimnasioDB", user, password)) {
                    ejecutarScriptSQL(connGimnasio, "bdgimnasio.sql");
                }
            }

            conexion = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/GimnasioDB", user, password);
            System.out.println("Conectado a la base de datos 'GimnasioDB'.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean baseDeDatosExiste(Connection conn, String dbName) {
        try (ResultSet resultSet = conn.getMetaData().getCatalogs()) {
            while (resultSet.next()) {
                if (dbName.equals(resultSet.getString(1))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al comprobar la existencia de la base de datos: " + e.getMessage());
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

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }

                sb.append(line);
                if (line.endsWith(";")) {
                    try {
                        stmt.executeUpdate(sb.toString());
                    } catch (SQLException e) {
                        if (!e.getMessage().contains("already exists")) {
                            System.out.println("Error al ejecutar el script SQL: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    sb = new StringBuilder();
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error al ejecutar el script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos para operar con la tabla Clientes
    public void insertarCliente(String nombre, String apellido, LocalDate fechaNacimiento, String email, String telefono, String direccion) {
        String sentenciaSql = "INSERT INTO Clientes (nombre, apellido, fecha_nacimiento, email, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        // Implementa el resto de la lógica para insertar un cliente
    }

    // Implementa métodos similares para actualizar y eliminar clientes
    // ...

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";
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

    private void setPropValues(String ip, String user, String pass, String adminPassword) {
        OutputStream output = null;
        try {
            Properties prop = new Properties();

            // Establece las nuevas propiedades
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPassword);

            // Guarda las propiedades en un archivo
            output = new FileOutputStream("config.properties");
            prop.store(output, null);

            // Actualiza las variables de instancia con los nuevos valores
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


    // Puedes añadir más métodos para trabajar con otras tablas de tu base de datos
    // ...
}
