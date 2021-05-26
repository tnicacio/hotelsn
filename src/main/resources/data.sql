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
INSERT INTO tb_room (name) VALUES ('301');
INSERT INTO tb_room (name) VALUES ('302');

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

INSERT INTO tb_booking (person_id, room_id, garage_id, start_Date, end_Date, dt_Checkin, dt_Checkout, expected_price, real_price) VALUES
(1, 1, null, TIMESTAMP WITH TIME ZONE '2021-05-13T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-21T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-13T13:00:00Z', TIMESTAMP WITH TIME ZONE '2021-05-21T20:00:00Z', 1140.0, 1290.0),
(2, 2, null, TIMESTAMP WITH TIME ZONE '2021-05-14T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-14T12:35:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T16:30:00Z', 420.0, 420.0),
(3, 3, 1, TIMESTAMP WITH TIME ZONE '2021-05-15T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-15T12:40:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T17:00:00Z', 340.0, 340.0),
(4, 5, 2, TIMESTAMP WITH TIME ZONE '2021-05-15T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-19T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-15T12:32:00Z', TIMESTAMP WITH TIME ZONE '2021-05-19T19:45:00Z', 745.0, 880.0),
(5, 6, 3, TIMESTAMP WITH TIME ZONE '2021-05-16T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-18T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-16T13:15:00Z', TIMESTAMP WITH TIME ZONE '2021-05-18T17:30:00Z', 440.0, 440.0),
(3, 2, 4, TIMESTAMP WITH TIME ZONE '2021-05-17T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-24T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-17T12:20:00Z', null, 1150.0, null),
(6, 3, 2, TIMESTAMP WITH TIME ZONE '2021-05-22T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-25T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-22T12:37:00Z', null, 610.0, null),
(7, 1, 1, TIMESTAMP WITH TIME ZONE '2021-05-23T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-24T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-23T13:30:00Z', null, 305.0, null),
(8, 4, null, TIMESTAMP WITH TIME ZONE '2021-05-23T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-25T19:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-23T13:35:00Z', null, 540.0, null),
(9, 5, 6, TIMESTAMP WITH TIME ZONE '2021-05-24T12:30:00Z', TIMESTAMP WITH TIME ZONE '2021-05-26T19:30:00Z', null, null, 405.0, null);

UPDATE tb_room SET is_Available = false WHERE id in (1,2,3,4);

UPDATE tb_garage SET is_Available = false WHERE id in (1,2,3,4);
