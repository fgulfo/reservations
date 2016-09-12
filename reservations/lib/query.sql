CREATE TABLE "usuarios" (
"id" serial4 NOT NULL,
"nombre" varchar(255),
"password" varchar(255),
"perfil" int4 NOT NULL,
"tercero" int4 NOT NULL,
"estado" int4 NOT NULL,
CONSTRAINT "idusuario_pkey" PRIMARY KEY ("id") 
);

DROP TABLE usuarios;
DROP TABLE reservas;
CREATE TABLE "reservas"(
"id" serial4 NOT NULL,
"nombredependencia" varchar(255),
"idusuario" int4,
"tituloevento" varchar(255),
"fechaeventodesde" timestamp(6),
"fechaeventohasta" timestamp(6),
"estadoevento" int4 NOT NULL,
"idtipoevento" int4 NOT NULL,
"estado" int4 NOT NULL,
CONSTRAINT "idreserva_pkey" PRIMARY KEY ("id") 
);

ALTER TABLE "reservas" ADD CONSTRAINT "reservas_usuarios_fkey" FOREIGN KEY ("idusuario") REFERENCES "usuarios" ("id");
ALTER TABLE "reservas" ADD CONSTRAINT "reservas_tipoeventos_fkey" FOREIGN KEY ("idtipoevento") REFERENCES "tipoeventos" ("id");
drop table tipoeventos
CREATE TABLE "tipoeventos"(
"id" serial4,
"nombre" varchar(255),
"estado" int4 NOT NULL,
CONSTRAINT "idtipoevento_pkey" PRIMARY KEY ("id") 
);

insert into usuarios values(default,'fgulfo','123',1,1,1);
insert into tipoeventos values(default,'Reunion',1);
insert into tipoeventos values(default,'Cumpleaños',1);
update usuarios set estado = 1
select * from usuarios
select * from tipoeventos
select * from reservas