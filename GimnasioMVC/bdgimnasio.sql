-- Creación de la tabla Clientes
CREATE TABLE Clientes
(
    id_cliente       INT PRIMARY KEY AUTO_INCREMENT,
    nombre           VARCHAR(50),
    apellido         VARCHAR(50),
    fecha_nacimiento DATE,
    email            VARCHAR(100),
    telefono         VARCHAR(15),
    direccion        VARCHAR(100)
);

-- Creación de la tabla Suscripciones
CREATE TABLE Suscripciones
(
    id_suscripcion INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente     INT,
    fecha_inicio   DATE,
    fecha_fin      DATE,
    tipo           VARCHAR(50),
    precio         DECIMAL(10, 2),
    estado_pago    BOOLEAN,
    FOREIGN KEY (id_cliente) REFERENCES Clientes (id_cliente)
);

-- Creación de la tabla Clases
CREATE TABLE Clases
(
    id_clase         INT PRIMARY KEY AUTO_INCREMENT,
    nombre           VARCHAR(50),
    tipo             VARCHAR(50),
    capacidad_maxima INT,
    duracion         TIME,
    instructor       VARCHAR(50)
);

-- Creación de la tabla InscripcionesClases
CREATE TABLE InscripcionesClases
(
    id_inscripcion    INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente        INT,
    id_clase          INT,
    fecha_inscripcion DATE,
    asistio           BOOLEAN,
    FOREIGN KEY (id_cliente) REFERENCES Clientes (id_cliente),
    FOREIGN KEY (id_clase) REFERENCES Clases (id_clase)
);

-- Creación de la tabla Entrenadores
CREATE TABLE Entrenadores
(
    id_entrenador      INT PRIMARY KEY AUTO_INCREMENT,
    nombre             VARCHAR(50),
    especialidad       VARCHAR(50),
    horas_disponibles  INT,
    fecha_contratacion DATE,
    salario            DECIMAL(10, 2)
);

-- Creación de la tabla Equipamiento
CREATE TABLE Equipamiento
(
    id_equipamiento INT PRIMARY KEY AUTO_INCREMENT,
    tipo            VARCHAR(50),
    marca           VARCHAR(50),
    fecha_compra    DATE,
    costo           DECIMAL(10, 2),
    estado          VARCHAR(50)
);

-- Creación de la tabla Mantenimientos
CREATE TABLE Mantenimientos
(
    id_mantenimiento    INT PRIMARY KEY AUTO_INCREMENT,
    id_equipamiento     INT,
    fecha_mantenimiento DATE,
    descripcion         VARCHAR(100),
    costo               DECIMAL(10, 2),
    FOREIGN KEY (id_equipamiento) REFERENCES Equipamiento (id_equipamiento)
);
