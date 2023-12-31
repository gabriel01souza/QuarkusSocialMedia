CREATE DATABASE quarkus-social;

CREATE TABLE users (
    id bigserial not null primary key,
    name varchar(100) not null,
    age integer not null
)

CREATE TABLE posts (
    id bigserial not null primary key,
    description varchar(150) not null,
    dateTime timestamp not null,
    user_id bigint not null references users(id)
)