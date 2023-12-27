create table clientes
(
    id_cliente       int primary key auto_increment,
    nombre           varchar(50),
    apellido         varchar(50),
    fecha_nacimiento date,
    email            varchar(100),
    telefono         varchar(15),
    direccion        varchar(100)
);

-- creación de la tabla suscripciones
create table suscripciones
(
    id_suscripcion int primary key auto_increment,
    id_cliente     int,
    fecha_inicio   date,
    fecha_fin      date,
    tipo           varchar(50),
    precio         decimal(10, 2),
    estado_pago    boolean,
    foreign key (id_cliente) references clientes (id_cliente)
);

-- creación de la tabla clases relacionada con la tabla entrenadores
create table clases
(
    id_clase         int primary key auto_increment,
    nombre           varchar(50),
    tipo             varchar(50),
    capacidad_maxima int,
    duracion         time,
    instructor       varchar(50)
);

-- creación de la tabla entrenadores
create table entrenadores
(
    id_entrenador      int primary key auto_increment,
    nombre             varchar(50),
    especialidad       varchar(50),
    horas_disponibles  int,
    fecha_contratacion date,
    salario            decimal(10, 2)
);

-- añadimos la columna id_entrenador a la tabla clases para poder relacionarla con la tabla entrenadores
alter table clases
    add id_entrenador int,
    add foreign key (id_entrenador) references entrenadores (id_entrenador);

-- creación de la tabla equipamiento relacionada con la tabla mantenimientos
create table equipamiento
(
    id_equipamiento int primary key auto_increment,
    tipo            varchar(50),
    marca           varchar(50),
    fecha_compra    date,
    costo           decimal(10, 2),
    estado          varchar(50)
);

-- creación de la tabla mantenimientos relacionada con la tabla equipamiento
create table mantenimientos
(
    id_mantenimiento    int primary key auto_increment,
    id_equipamiento     int,
    fecha_mantenimiento date,
    descripcion         varchar(100),
    costo               decimal(10, 2),
    foreign key (id_equipamiento) references equipamiento (id_equipamiento)
);

delimiter ||
create function ganancias_por_cliente_add (num_clientes int, tipo_suscripcion varchar(50))
    returns decimal(10, 2)
begin
    declare ganancias decimal(10, 2);
    if tipo_suscripcion = "básica" then
        set ganancias = num_clientes * 20.00;
    elseif tipo_suscripcion = "premium" then
        set ganancias = num_clientes * 35.00;
    elseif tipo_suscripcion = "familiar" then
        set ganancias = num_clientes * 50.00;
    elseif tipo_suscripcion = "estudiante" then
        set ganancias = num_clientes * 15.00;
    end if;
    return ganancias;
end; ||
delimiter ;
