CREATE TABLE person (
    id bigint not null auto_increment,
    first_name varchar(80) not null,
    last_name varchar(80) not null,
    address varchar(100) not null,
    gender varchar(6) not null,
    PRIMARY KEY (id)
);