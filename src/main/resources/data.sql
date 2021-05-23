INSERT INTO tb_garage (name) VALUES ('A01');
INSERT INTO tb_garage (name) VALUES ('A02');
INSERT INTO tb_garage (name) VALUES ('B01');
INSERT INTO tb_garage (name) VALUES ('B02');
INSERT INTO tb_garage (name) VALUES ('C01');
INSERT INTO tb_garage (name) VALUES ('C02');
INSERT INTO tb_garage (name) VALUES ('C03');
INSERT INTO tb_garage (name) VALUES ('C04');
INSERT INTO tb_garage (name) VALUES ('D01');
INSERT INTO tb_garage (name, is_Available) VALUES ('D02', false);

INSERT INTO tb_room (name) VALUES ('101');
INSERT INTO tb_room (name) VALUES ('102');
INSERT INTO tb_room (name) VALUES ('103');
INSERT INTO tb_room (name) VALUES ('104');
INSERT INTO tb_room (name) VALUES ('201');
INSERT INTO tb_room (name) VALUES ('202');

INSERT INTO tb_person (name, email, age) VALUES ('Maria Joana', 'maria@email.com', 24);
INSERT INTO tb_person (name, email, age) VALUES ('Pedro Paulo', 'pedrop@email.com', 26);
INSERT INTO tb_person (name, email, age) VALUES ('Mario Silva', 'marios@email.com', 21);
INSERT INTO tb_person (name, email, age) VALUES ('Valentina Enza', 'valenza@email.com', 21);
INSERT INTO tb_person (name, email, age) VALUES ('Enzo Zone', 'zone@email.com', 23);
INSERT INTO tb_person (name, email, age) VALUES ('Klaus Santa', 'papainoel@email.com', 56);
INSERT INTO tb_person (name, email, age) VALUES ('Nora Jones', 'nora.jones@email.com', 29);
INSERT INTO tb_person (name, email, age) VALUES ('Wanderson Wallace', 'wwemail@email.com', 32);
INSERT INTO tb_person (name, email, age) VALUES ('Ana Furtado', 'ana.furtado@email.com', 41);
INSERT INTO tb_person (name, email, age) VALUES ('Patrícia Poeta', 'poeta.patty@email.com', 40);
INSERT INTO tb_person (name, email, age) VALUES ('Guilherme Briggs', 'briggs.gui@email.com', 46);
INSERT INTO tb_person (name, email, age) VALUES ('Raul Santos Silva Souto', 'rsss@email.com', 18);
INSERT INTO tb_person (name, email, age) VALUES ('Ítalo Filho', 'it.filho@email.com', 35);

INSERT INTO tb_guest (person_id, room_id, garage_id, start_Date, end_Date) VALUES
(1, 1, null, TIMESTAMP WITH TIME ZONE '2021-05-13T10:00:00Z', TIMESTAMP WITH TIME ZONE '2021-05-21T17:00:00Z'),
(2, 2, null, TIMESTAMP WITH TIME ZONE '2021-05-14T09:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T13:30:00Z'),
(3, 3, 1, TIMESTAMP WITH TIME ZONE '2021-05-15T09:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T14:00:00Z'),
(4, 5, 2, TIMESTAMP WITH TIME ZONE '2021-05-15T09:32:00Z', TIMESTAMP WITH TIME ZONE '2021-05-19T18:45:00Z'),
(5, 6, 3, TIMESTAMP WITH TIME ZONE '2021-05-16T08:15:00Z', TIMESTAMP WITH TIME ZONE '2021-05-18T14:30:00Z'),
(3, 2, 4, TIMESTAMP WITH TIME ZONE '2021-05-17T09:20:00Z', null),
(6, 3, 2, TIMESTAMP WITH TIME ZONE '2021-05-22T08:40:00Z', null),
(7, 1, 1, TIMESTAMP WITH TIME ZONE '2021-05-23T10:30:00Z', null),
(8, 4, 3, TIMESTAMP WITH TIME ZONE '2021-05-23T10:35:00Z', null),
(9, 5, null, TIMESTAMP WITH TIME ZONE '2021-05-23T11:10:00Z', null);

UPDATE tb_room SET is_Available = false WHERE id in (1,2,3,4,5);

UPDATE tb_garage SET is_Available = false WHERE id in (1,2,3,4);

