package gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

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

    void conectar() {
        //pendiente completar
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://" + ip +
                    ":3306/mibase", user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            ;
        }
    }

    private String leerFichero() {
        return "";
    }

    void desconectar() {

    }

    void insertarAutor(String nombre, String apellidos,
                       LocalDate fechaNacimiento, String pais) {
        String sentenciaSql = "INSERT INTO autores(nombre, apellidos," +
                "fechanacimiento, pais) VALUES (?,?,?,?)";
        PreparedStatement sentencia = null;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, pais);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    void insertarEditorial(String editorial, String email, String telefono,
                           String tipoEditorial, String web) {
        String sentenciaSql = "INSERT INTO editoriales(editorial, email," +
                "telefono, tipoeditorial,web) VALUES (?,?,?,?,?)";
        PreparedStatement sentencia = null;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, editorial);
            sentencia.setString(2, email);
            sentencia.setString(3, telefono);
            sentencia.setString(4, tipoEditorial);
            sentencia.setString(5, web);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void insertarLibro(String titulo, String isbn, String editorial,
                       String genero, String autor, float precio,
                       LocalDate fechaLanzamiento) {
        String sentenciaSql = "INSERT INTO libros(titulo, isbn," +
                "ideditorial, genero,idautor,precio,fechalanzamiento) " +
                "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement sentencia = null;

        int ideditorial = Integer.parseInt(editorial.split(" ")[0]);
        int idautor = Integer.parseInt(autor.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, titulo);
            sentencia.setString(2, isbn);
            sentencia.setInt(3, ideditorial);
            sentencia.setString(4, genero);
            sentencia.setInt(5, idautor);
            sentencia.setFloat(6, precio);
            sentencia.setDate(7, Date.valueOf(fechaLanzamiento));
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void modificarEditorial(String editorial, String email, String telefono,
                            String tipoEditorial, String web, int ideditorial) {
        String sentenciaSql = "UPDATE editoriales SET editorial=?,email=?,telefono=?," +
                "tipoeditorial=?,web=? WHERE ideditorial=?";
        PreparedStatement sentencia = null;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, editorial);
            sentencia.setString(2, email);
            sentencia.setString(3, telefono);
            sentencia.setString(4, tipoEditorial);
            sentencia.setString(5, web);
            sentencia.setInt(6, ideditorial);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void modificarAutor(String nombre, String apellidos,
                        LocalDate fechaNacimiento, String pais, int idautor) {
        String sentenciaSql = "UPDATE autores SET nombre=?,apellidos=?," +
                "fechanacimiento=?,pais=? WHERE idautor=?";
        PreparedStatement sentencia = null;
        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setDate(3, Date.valueOf(fechaNacimiento));
            sentencia.setString(4, pais);
            sentencia.setInt(5, idautor);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    void modificarLibro(String titulo, String isbn, String editorial,
                        String genero, String autor, float precio,
                        LocalDate fechaLanzamiento, int idlibro) {
        String sentenciaSql = "UPDATE libros SET titulo=?,isbn=?,ideditorial=?," +
                "genero=?,idautor=?,precio=?,fechalanzamiento=?" +
                "WHERE idlibro=?";
        PreparedStatement sentencia = null;

        int ideditorial = Integer.parseInt(editorial.split(" ")[0]);
        int idautor = Integer.parseInt(autor.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, titulo);
            sentencia.setString(2, isbn);
            sentencia.setInt(3, ideditorial);
            sentencia.setString(4, genero);
            sentencia.setInt(5, idautor);
            sentencia.setFloat(6, precio);
            sentencia.setDate(7, Date.valueOf(fechaLanzamiento));
            sentencia.setInt(8, idlibro);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    void borrarEditorial(int ideditorial) {
        String sentenciaSql = "DELETE FROM editoriales WHERE ideditorial=?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, ideditorial);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void borrarAutor(int idautor) {
        String sentenciaSql = "DELETE FROM autores WHERE idautor=?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idautor);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void borrarLibro(int idlibro) {
        String sentenciaSql = "DELETE FROM libros WHERE idlibro=?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idlibro);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sentencia != null) {
                try {
                    sentencia.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void consultarAutor() {

    }

    void consultarEditorial() {

    }

    void consultarLibros() {

    }

    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Archivo de propiedades 'config.properties' no encontrado en el classpath");
            }

            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");
        } catch (IOException e) {
            System.out.println("Excepción " + e);
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
        try {
            Properties prop = new Properties();
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPassword);
            OutputStream out = new FileOutputStream("config.properties");
            prop.store(out, null);
        } catch (IOException e) {
            System.out.println("Excepcion " + e);
        }
        this.ip = ip;
        this.user = user;
        this.password = pass;
        this.adminPassword = adminPassword;
    }

    public boolean libroIsbnYaExiste(String isbn) {
        String salesConsult = "SELECT existeIsbn(?)";
        PreparedStatement function;
        boolean isbnExists = false;
        try {
            function = conexion.prepareStatement(salesConsult);
            function.setString(1, isbn);
            ResultSet rs = function.executeQuery();
            rs.next();
            isbnExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isbnExists;
    }

    public boolean editorialNombreYaExiste(String nombre) {
        String editorialNameConsult = "SELECT existeNombreEditorial(?)";
        PreparedStatement function;
        boolean nameExists = false;
        try {
            function = conexion.prepareStatement(editorialNameConsult);
            function.setString(1, nombre);
            ResultSet rs = function.executeQuery();
            rs.next();
            nameExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameExists;
    }

    public boolean autorNombreYaExiste(String nombre, String apellidos) {
        String completeName = apellidos + ", " + nombre;
        String authorNameConsult = "SELECT existeNombreAutor(?)";
        PreparedStatement function;
        boolean nameExists = false;
        try {
            function = conexion.prepareStatement(authorNameConsult);
            function.setString(1, completeName);
            ResultSet rs = function.executeQuery();
            rs.next();
            nameExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameExists;
    }
}
