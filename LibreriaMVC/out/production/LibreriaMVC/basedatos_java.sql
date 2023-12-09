CREATE DATABASE if not exists mibase;
--
USE mibase;
--
CREATE TABLE IF NOT EXISTS autores (
idautor int auto_increment primary key,
nombre varchar(50) not null,
apellidos varchar(150) not null,
fechanacimiento date,
pais varchar(50));
--
CREATE TABLE IF NOT EXISTS editoriales (
ideditorial int auto_increment primary key,
editorial varchar(50)  not null,
email varchar(100) not null,
telefono varchar(9),
tipoeditorial varchar(50),
web varchar(500));
--
CREATE TABLE IF NOT EXISTS libros (
idlibro int auto_increment primary key,
titulo varchar(50) not null,
isbn varchar(40) not null UNIQUE,
ideditorial int not null,
genero varchar(30),
idautor int not null,
precio float not null,
fechalanzamiento date);
--
alter table libros
add foreign key (ideditorial) references editoriales(ideditorial),
add foreign key (idautor) references autores(idautor);
--
create function existeIsbn(f_isbn varchar(40))
returns bit
begin
	declare i int;
	set i=0;
	while (i<(select max(idlibro) from libros)) do
	if ((select isbn from libros 
		 where idlibro=(i+1)) like f_isbn) 
	then return 1;
	end if;
	set i=i+1;
	end while;
	return 0;
end; 
--
create function existeNombreEditorial (f_name varchar(50))
returns bit
begin
	declare i int;
	set i=0;
	while (i<(select max(ideditorial) from editoriales)) do
	if ((select editorial from editoriales 
	     where ideditorial = (i+1)) like f_name) 
	then return 1;
	end if;
	set i=i+1;
	end while;
	return 0;
end; 
--
create function existeNombreAutor (f_name varchar(50))
returns bit
begin
	declare i int;
	set i=0;
	while (i<(select max(idautor) from autores)) do
	if ((select concat(apellidos,', ',nombre) from autores
		where idautor = (i+1)) like f_name)
	then return 1;
	end if;
	set i=i+1;
	end while;
	return 0;
end; 


