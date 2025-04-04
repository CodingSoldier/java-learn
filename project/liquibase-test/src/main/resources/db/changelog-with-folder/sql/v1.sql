-- changeset your.name:zxf21 labels:example-label context:example-context
-- comment: example comment
create table PERSON111121
(
    ID    int          not null,
    FNAME varchar(100) not null
);

-- changeset your.name:zxf22 labels:example-label context:example-context
-- comment: example comment
create table company122
(
    id       int primary key auto_increment not null,
    name     varchar(50)                    not null,
    address1 varchar(50),
    address2 varchar(50),
    city     varchar(30)
);