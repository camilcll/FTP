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
	id int not null,
	positionCalculee json not null,
	zone int not null,
	intensiteCalculee int not null,
	PRIMARY KEY (id)
)

CREATE TABLE Caserne(
	id int not null,
	position json not null,
	listeVehicule json not null,
	PRIMARY KEY (id)
)

CREATE TABLE Capteur(
	id int not null,
	position json not null,
	intensite int not null,
	range int not null,
	PRIMARY KEY (id)
)

CREATE TABLE Vehicule(
	id int not null,
	type varchar(25) not null,
	idCaserne int not null,
	disponible boolean not null,
	PRIMARY KEY (id),
	CONSTRAINT FK_Caserne
		FOREIGN KEY (idCaserne)
		REFERENCES Caserne(id)
);

CREATE TABLE Intervention(
	id int not null,
	feuCalcule json not null,
	listeVehicule json not null,
	etat int not null,
	PRIMARY KEY (id)
);