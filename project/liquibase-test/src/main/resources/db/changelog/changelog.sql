-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE `test_table`
(
    `test_id`     int          NOT NULL,
    `test_column` varchar(255) NULL,
    PRIMARY KEY (`test_id`)
);

-- changeset your.name:1 labels:example-label context:example-context
-- comment: example comment
create table person
(
    id       int primary key auto_increment not null,
    name     varchar(50)                    not null,
    address1 varchar(50),
    address2 varchar(50),
    city     varchar(30)
)

-- changeset your.name:22 labels:example-label context:example-context
-- comment: example comment
create table company
(
    id       int primary key auto_increment not null,
    name     varchar(50)                    not null,
    address1 varchar(50),
    address2 varchar(50),
    city     varchar(30)
)
-- rollback DROP TABLE company;

-- changeset your.name:23 labels:example-label context:example-context
-- comment: example comment
create table company1
(
    id       int primary key auto_increment not null,
    name     varchar(50)                    not null,
    address1 varchar(50),
    address2 varchar(50),
    city     varchar(30)
);
-- rollback DROP TABLE company1;