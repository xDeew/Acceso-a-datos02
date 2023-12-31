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

create table suscripciones
(
    id_suscripcion int primary key auto_increment,
    id_cliente     int,
    fecha_inicio   date,
    fecha_fin      date,
    tipo           varchar(50),
    precio         decimal(10, 2),
    estado_pago    boolean
);

alter table suscripciones
    add foreign key (id_cliente) references clientes (id_cliente);

create table entrenadores
(
    id_entrenador      int primary key auto_increment,
    nombre             varchar(50),
    especialidad       varchar(50),
    fecha_contratacion date,
    salario            decimal(10, 2)
);

create table equipamiento
(
    id_equipamiento int primary key auto_increment,
    tipo            varchar(50),
    marca           varchar(50),
    fecha_compra    date,
    costo           decimal(10, 2),
    estado          varchar(50)
);

create table clases
(
    id_clase           int primary key auto_increment,
    tipo               varchar(50),
    capacidad_maxima   int,
    duracion           time,
    instructor         varchar(50),
    material_utilizado varchar(255),
    id_entrenador      int,
    id_equipamiento    int
);

alter table clases
    add foreign key (id_entrenador) references entrenadores (id_entrenador),
    add foreign key (id_equipamiento) references equipamiento (id_equipamiento);

delimiter ||
create function ganancias_por_cliente_add(num_clientes int, tipo_suscripcion varchar(50))
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
