DROP DATABASE okidak;
CREATE DATABASE okidak;
USE okidak;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE departements(
    id_departement SERIAL PRIMARY KEY,
    name VARCHAR(75)
);

CREATE TABLE type_campaign(
    id_type_campaign SERIAL PRIMARY KEY,
    name VARCHAR(75)
);

CREATE TABLE annonceurs(
    id_annonceur BIGSERIAL PRIMARY KEY,
    name VARCHAR(75),
    adresse VARCHAR(250),
    id_departement INT NOT NULL REFERENCES departements(id_departement)
);

CREATE TABLE campaigns(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    status INT DEFAULT 0,
    datePublication DATE,
    id_type_campaign INT NOT NULL REFERENCES type_campaign(id_type_campaign),
    id_annonceur INT NOT NULL REFERENCES annonceurs(id_annonceur)
); 

