create table topicos (

     id bigint not null auto_increment,
     titulo varchar(255) not null unique,
     mensaje text not null,
     fecha_creacion datetime not null,
     status varchar(50) not null,
     autor varchar(100) not null,
     curso varchar(100) not null,

     primary key(id),
     unique(titulo, mensaje(255))
) engine=InnoDB default charset=utf8mb4;
