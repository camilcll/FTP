CREATE TABLE Feu(
	id int not null,
	position json not null,
	intensite int not null,
	detecte boolean not null,
	PRIMARY KEY (id)
)

CREATE TABLE Capteur(
	id int not null,
	position json not null,
	intensite int not null,
	range int not null,
	PRIMARY KEY (id)
)

insert into Capteur(id,intensite,position,range) values (1, 0, '{"x":5,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (2, 0, '{"x":15,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (3, 0, '{"x":25,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (4, 0, '{"x":35,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (5, 0, '{"x":45,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (6, 0, '{"x":55,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (7, 0, '{"x":65,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (8, 0, '{"x":75,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (9, 0, '{"x":85,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (10, 0, '{"x":95,"y":5}', 5);
insert into Capteur(id,intensite,position,range) values (11, 0, '{"x":5,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (12, 0, '{"x":15,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (13, 0, '{"x":25,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (14, 0, '{"x":35,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (15, 0, '{"x":45,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (16, 0, '{"x":55,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (17, 0, '{"x":65,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (18, 0, '{"x":75,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (19, 0, '{"x":85,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (20, 0, '{"x":95,"y":15}', 5);
insert into Capteur(id,intensite,position,range) values (21, 0, '{"x":5,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (22, 0, '{"x":15,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (23, 0, '{"x":25,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (24, 0, '{"x":35,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (25, 0, '{"x":45,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (26, 0, '{"x":55,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (27, 0, '{"x":65,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (28, 0, '{"x":75,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (29, 0, '{"x":85,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (30, 0, '{"x":95,"y":25}', 5);
insert into Capteur(id,intensite,position,range) values (31, 0, '{"x":5,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (32, 0, '{"x":15,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (33, 0, '{"x":25,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (34, 0, '{"x":35,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (35, 0, '{"x":45,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (36, 0, '{"x":55,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (37, 0, '{"x":65,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (38, 0, '{"x":75,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (39, 0, '{"x":85,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (40, 0, '{"x":95,"y":35}', 5);
insert into Capteur(id,intensite,position,range) values (41, 0, '{"x":5,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (42, 0, '{"x":15,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (43, 0, '{"x":25,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (44, 0, '{"x":35,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (45, 0, '{"x":45,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (46, 0, '{"x":55,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (47, 0, '{"x":65,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (48, 0, '{"x":75,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (49, 0, '{"x":85,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (50, 0, '{"x":95,"y":45}', 5);
insert into Capteur(id,intensite,position,range) values (51, 0, '{"x":5,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (52, 0, '{"x":15,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (53, 0, '{"x":25,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (54, 0, '{"x":35,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (55, 0, '{"x":45,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (56, 0, '{"x":55,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (57, 0, '{"x":65,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (58, 0, '{"x":75,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (59, 0, '{"x":85,"y":55}', 5);
insert into Capteur(id,intensite,position,range) values (60, 0, '{"x":95,"y":55}', 5);

--Emergency

CREATE TABLE Feu(
	"id" int not null,
	"positionCalculee" json not null,
	"zone" int not null,
	"intensiteCalculee" int not null,
	PRIMARY KEY (id)
)

CREATE TABLE Caserne(
	"id" int not null,
	"position" json not null,
	"listeVehicule" json not null,
	PRIMARY KEY (id)
)

CREATE TABLE Capteur(
	"id" int not null,
	"position" json not null,
	"intensite" int not null,
	"range" int not null,
	PRIMARY KEY (id)
)

CREATE TABLE Vehicule(
	"id" int not null,
	"type" varchar(25) not null,
	"position" json not null,
	"idcaserne" int not null,
	"disponible" boolean not null,
	PRIMARY KEY (id),
	CONSTRAINT FK_Caserne
		FOREIGN KEY ("idcaserne")
		REFERENCES Caserne(id)
);

CREATE TABLE Intervention(
	"id" int not null,
	"feu" json not null,
	"listeVehicule" json not null,
	"etat" int not null,
	PRIMARY KEY (id)
);

INSERT INTO caserne (id, position, "listeVehicule") VALUES (1,'{"x":15,"y":25}','[{"id":1,"type":"Camion","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":2,"type":"Camion","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":3,"type":"Camion","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":4,"type":"Camion","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":5,"type":"Camion","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":21,"type":"Voiture","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":22,"type":"Voiture","position":{"x":15,"y":25},"idcaserne":1,"disponible":true},{"id":23,"type":"Voiture","position":{"x":15,"y":25},"idcaserne":1,"disponible":true}]');
INSERT INTO caserne (id, position, "listeVehicule") VALUES (2,'{"x":45,"y":25}','[{"id":6,"type":"Camion","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":7,"type":"Camion","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":8,"type":"Camion","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":9,"type":"Camion","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":10,"type":"Camion","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":24,"type":"Voiture","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":25,"type":"Voiture","position":{"x":45,"y":25},"idcaserne":2,"disponible":true},{"id":26,"type":"Voiture","position":{"x":45,"y":25},"idcaserne":2,"disponible":true}]');
INSERT INTO caserne (id, position, "listeVehicule") VALUES (3,'{"x":45,"y":75}', '[{"id":11,"type":"Camion","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":12,"type":"Camion","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":13,"type":"Camion","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":14,"type":"Camion","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":15,"type":"Camion","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":27,"type":"Voiture","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":28,"type":"Voiture","position":{"x":45,"y":75},"idcaserne":3,"disponible":true},{"id":29,"type":"Voiture","position":{"x":45,"y":75},"idcaserne":3,"disponible":true}]');
INSERT INTO caserne (id, position, "listeVehicule") VALUES (4,'{"x":15,"y":75}', '[{"id":16,"type":"Camion","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":17,"type":"Camion","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":18,"type":"Camion","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":19,"type":"Camion","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":20,"type":"Camion","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":30,"type":"Voiture","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":31,"type":"Voiture","position":{"x":15,"y":75},"idcaserne":4,"disponible":true},{"id":32,"type":"Voiture","position":{"x":15,"y":75},"idcaserne":4,"disponible":true}]');



INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (1, 'Camion', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (2, 'Camion', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (3, 'Camion', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (4, 'Camion', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (5, 'Camion', '{"x":15,"y":25}', 1, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (6, 'Camion', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (7, 'Camion', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (8, 'Camion', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (9, 'Camion', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (10, 'Camion', '{"x":45,"y":25}', 2, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (11, 'Camion', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (12, 'Camion', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (13, 'Camion', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (14, 'Camion', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (15, 'Camion', '{"x":45,"y":75}', 3, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (16, 'Camion', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (17, 'Camion', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (18, 'Camion', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (19, 'Camion', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (20, 'Camion', '{"x":15,"y":75}', 4, true);




INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (21, 'Voiture', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (22, 'Voiture', '{"x":15,"y":25}', 1, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (23, 'Voiture', '{"x":15,"y":25}', 1, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (24, 'Voiture', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (25, 'Voiture', '{"x":45,"y":25}', 2, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (26, 'Voiture', '{"x":45,"y":25}', 2, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (27, 'Voiture', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (28, 'Voiture', '{"x":45,"y":75}', 3, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (29, 'Voiture', '{"x":45,"y":75}', 3, true);

INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (30, 'Voiture', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (31, 'Voiture', '{"x":15,"y":75}', 4, true);
INSERT INTO vehicule (id, type, position, idcaserne, disponible) VALUES (32, 'Voiture', '{"x":15,"y":75}', 4, true);
