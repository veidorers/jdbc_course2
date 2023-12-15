CREATE DATABASE flight_repository;

CREATE TABLE airport
(
    code CHAR(3) PRIMARY KEY ,
    country VARCHAR(256) NOT NULL ,
    city VARCHAR(128) NOT NULL
);

CREATE TABLE aircraft
(
    id INT AUTO_INCREMENT PRIMARY KEY ,
    model VARCHAR(128) NOT NULL
);

CREATE TABLE seat
(
    aircraft_id INT,
    seat_no VARCHAR(4) NOT NULL ,
    PRIMARY KEY (aircraft_id, seat_no),
    FOREIGN KEY (aircraft_id) REFERENCES aircraft (id)
);

CREATE TABLE flight
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    flight_no VARCHAR(16) NOT NULL ,
    departure_date TIMESTAMP NOT NULL ,
    departure_airport_code CHAR(3),
    arrival_date TIMESTAMP NOT NULL ,
    arrival_airport_code CHAR(3),
    aircraft_id INT,
    status VARCHAR(32) NOT NULL,
    FOREIGN KEY (departure_airport_code) REFERENCES airport(code),
    FOREIGN KEY (arrival_airport_code) REFERENCES airport(code),
    FOREIGN KEY (aircraft_id) REFERENCES aircraft (id)
);

CREATE TABLE ticket
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    passenger_no VARCHAR(32) NOT NULL ,
    passenger_name VARCHAR(128) NOT NULL ,
    flight_id BIGINT,
    seat_no VARCHAR(4) NOT NULL,
    cost DECIMAL(8, 2) NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES flight (id),
    UNIQUE KEY unique_flight_id_seat_no_idx (flight_id, seat_no)
);

insert into airport (code, country, city)
values ('MNK', 'Беларусь', 'Минск'),
       ('LDN', 'Англия', 'Лондон'),
       ('MSK', 'Россия', 'Москва'),
       ('BSL', 'Испания', 'Барселона');

insert into aircraft (model)
values ('Боинг 777-300'),
       ('Боинг 737-300'),
       ('Аэробус A320-200'),
       ('Суперджет-100');

INSERT INTO seat (aircraft_id, seat_no)
SELECT id, seat_no
FROM aircraft
         CROSS JOIN (SELECT 'A1' AS seat_no
                     UNION SELECT 'A2'
                     UNION SELECT 'B1'
                     UNION SELECT 'B2'
                     UNION SELECT 'C1'
                     UNION SELECT 'C2'
                     UNION SELECT 'D1'
                     UNION SELECT 'D2') s;


insert into flight (flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id,
                    status)
values
    ('MN3002', '2020-06-14 14:30:00', 'MNK', '2020-06-14 18:07:00', 'LDN', 1, 'ARRIVED'),
    ('MN3002', '2020-06-16 09:15:00', 'LDN', '2020-06-16 13:00:00', 'MNK', 1, 'ARRIVED'),
    ('BC2801', '2020-07-28 23:25:00', 'MNK', '2020-07-29 02:43:00', 'LDN', 2, 'ARRIVED'),
    ('BC2801', '2020-08-01 11:00:00', 'LDN', '2020-08-01 14:15:00', 'MNK', 2, 'DEPARTED'),
    ('TR3103', '2020-05-03 13:10:00', 'MSK', '2020-05-03 18:38:00', 'BSL', 3, 'ARRIVED'),
    ('TR3103', '2020-05-10 07:15:00', 'BSL', '2020-05-10 12:44:00', 'MSK', 3, 'CANCELLED'),
    ('CV9827', '2020-09-09 18:00:00', 'MNK', '2020-09-09 19:15:00', 'MSK', 4, 'SCHEDULED'),
    ('CV9827', '2020-09-19 08:55:00', 'MSK', '2020-09-19 10:05:00', 'MNK', 4, 'SCHEDULED'),
    ('QS8712', '2020-12-18 03:35:00', 'MNK', '2020-12-18 06:46:00', 'LDN', 2, 'ARRIVED');

insert into ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
values ('112233', 'Иван Иванов', 1, 'A1', 200),
       ('23234A', 'Петр Петров', 1, 'B1', 180),
       ('SS988D', 'Светлана Светикова', 1, 'B2', 175),
       ('QYASDE', 'Андрей Андреев', 1, 'C2', 175),
       ('POQ234', 'Иван Кожемякин', 1, 'D1', 160),
       ('898123', 'Олег Рубцов', 1, 'A2', 198),
       ('555321', 'Екатерина Петренко', 2, 'A1', 250),
       ('QO23OO', 'Иван Розмаринов', 2, 'B2', 225),
       ('9883IO', 'Иван Кожемякин', 2, 'C1', 217),
       ('123UI2', 'Андрей Буйнов', 2, 'C2', 227),
       ('SS988D', 'Светлана Светикова', 2, 'D2', 277),
       ('EE2344', 'Дмитрий Трусцов', 3, 'А1', 300),
       ('AS23PP', 'Максим Комсомольцев', 3, 'А2', 285),
       ('322349', 'Эдуард Щеглов', 3, 'B1', 99),
       ('DL123S', 'Игорь Беркутов', 3, 'B2', 199),
       ('MVM111', 'Алексей Щербин', 3, 'C1', 299),
       ('ZZZ111', 'Денис Колобков', 3, 'C2', 230),
       ('234444', 'Иван Старовойтов', 3, 'D1', 180),
       ('LLLL12', 'Людмила Старовойтова', 3, 'D2', 224),
       ('RT34TR', 'Степан Дор', 4, 'A1', 129),
       ('999666', 'Анастасия Шепелева', 4, 'A2', 152),
       ('234444', 'Иван Старовойтов', 4, 'B1', 140),
       ('LLLL12', 'Людмила Старовойтова', 4, 'B2', 140),
       ('LLLL12', 'Роман Дронов', 4, 'D2', 109),
       ('112233', 'Иван Иванов', 5, 'C2', 170),
       ('NMNBV2', 'Лариса Тельникова', 5, 'C1', 185),
       ('DSA586', 'Лариса Привольная', 5, 'A1', 204),
       ('DSA583', 'Артур Мирный', 5, 'B1', 189),
       ('DSA581', 'Евгений Кудрявцев', 6, 'A1', 204),
       ('EE2344', 'Дмитрий Трусцов', 6, 'A2', 214),
       ('AS23PP', 'Максим Комсомольцев', 6, 'B2', 176),
       ('112233', 'Иван Иванов', 6, 'B1', 135),
       ('309623', 'Татьяна Крот', 6, 'C1', 155),
       ('319623', 'Юрий Дувинков', 6, 'D1', 125),
       ('322349', 'Эдуард Щеглов', 7, 'A1', 69),
       ('DIOPSL', 'Евгений Безфамильная', 7, 'A2', 58),
       ('DIOPS1', 'Константин Швец', 7, 'D1', 65),
       ('DIOPS2', 'Юлия Швец', 7, 'D2', 65),
       ('1IOPS2', 'Ник Говриленко', 7, 'C2', 73),
       ('999666', 'Анастасия Шепелева', 7, 'B1', 66),
       ('23234A', 'Петр Петров', 7, 'C1', 80),
       ('QYASDE', 'Андрей Андреев', 8, 'A1', 100),
       ('1QAZD2', 'Лариса Потемнкина', 8, 'A2', 89),
       ('5QAZD2', 'Карл Хмелев', 8, 'B2', 79),
       ('2QAZD2', 'Жанна Хмелева', 8, 'С2', 77),
       ('BMXND1', 'Светлана Хмурая', 8, 'В2', 94),
       ('BMXND2', 'Кирилл Сарычев', 8, 'D1', 81),
       ('SS988D', 'Светлана Светикова', 9, 'A2', 222),
       ('SS978D', 'Андрей Желудь', 9, 'A1', 198),
       ('SS968D', 'Дмитрий Воснецов', 9, 'B1', 243),
       ('SS958D', 'Максим Гребцов', 9, 'С1', 251),
       ('112233', 'Иван Иванов', 9, 'С2', 135),
       ('NMNBV2', 'Лариса Тельникова', 9, 'B2', 217),
       ('23234A', 'Петр Петров', 9, 'D1', 189),
       ('123951', 'Полина Зверева', 9, 'D2', 234);


ALTER TABLE aircraft
    ADD COLUMN image BLOB;
