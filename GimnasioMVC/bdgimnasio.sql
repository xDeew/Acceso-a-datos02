-- Creación de la tabla Clientes Relacionada con la tabla Suscripciones
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

-- Creación de la tabla Clases Relacionada con la tabla Entrenadores
CREATE TABLE Clases
(
    id_clase         INT PRIMARY KEY AUTO_INCREMENT,
    nombre           VARCHAR(50),
    tipo             VARCHAR(50),
    capacidad_maxima INT,
    duracion         TIME,
    instructor       VARCHAR(50)
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

-- Añadimos la columna id_entrenador a la tabla Clases para poder relacionarla con la tabla Entrenadores
ALTER TABLE Clases
    ADD id_entrenador INT,
    ADD FOREIGN KEY (id_entrenador) REFERENCES Entrenadores (id_entrenador);


-- Creación de la tabla Equipamiento Relacionada con la tabla Mantenimientos
CREATE TABLE Equipamiento
(
    id_equipamiento INT PRIMARY KEY AUTO_INCREMENT,
    tipo            VARCHAR(50),
    marca           VARCHAR(50),
    fecha_compra    DATE,
    costo           DECIMAL(10, 2),
    estado          VARCHAR(50)
);

-- Creación de la tabla Mantenimientos Relacionada con la tabla Equipamiento
CREATE TABLE Mantenimientos
(
    id_mantenimiento    INT PRIMARY KEY AUTO_INCREMENT,
    id_equipamiento     INT,
    fecha_mantenimiento DATE,
    descripcion         VARCHAR(100),
    costo               DECIMAL(10, 2),
    FOREIGN KEY (id_equipamiento) REFERENCES Equipamiento (id_equipamiento)
);


