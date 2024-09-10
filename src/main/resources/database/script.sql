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

CREATE TABLE tarif_vue_campaign(
    id BIGSERIAL PRIMARY KEY,
    tarif DECIMAL,
    vue BIGINT
);


CREATE TABLE annonceurs(
    id_annonceur BIGSERIAL PRIMARY KEY,
    name VARCHAR(75),
    adresse VARCHAR(250),
    id_departement INT NOT NULL REFERENCES departements(id_departement)
);

CREATE TABLE campaign_video(
    id_campaign_video BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    urlVideo TEXT,
    logo_begin VARCHAR(255) NOT NULL,
    logo_end VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE campaigns(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    status INT DEFAULT 0,       
    date_creation TIMESTAMP,
    date_modification TIMESTAMP,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP,
    budget DECIMAL,
    vue_max BIGINT,
    id_type_campaign INT NOT NULL REFERENCES type_campaign(id_type_campaign),
    id_campaign_video BIGINT NOT NULL REFERENCES campaign_video(id_campaign_video),
    id_annonceur BIGINT NOT NULL REFERENCES annonceurs(id_annonceur)
); 



CREATE TABLE campaign_periode(
    id_campaign_periode BIGSERIAL PRIMARY KEY,
    ordre BIGINT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    budget_periode DECIMAL,
    vue_objectif BIGINT,
    id_campaign BIGINT NOT NULL REFERENCES campaigns(id)
);



CREATE TABLE campaign_carousel(
    id_campaign_carousel BIGSERIAL PRIMARY KEY,
    urlImage TEXT,
    id_campaign BIGINT NOT NULL REFERENCES campaigns(id)
);


CREATE TABLE statistic(
    id_stat BIGSERIAL PRIMARY KEY,
    id_campaign BIGINT NOT NULL REFERENCES campaigns(id),
    id_campaign_video BIGINT NOT NULL REFERENCES campaign_video(id_campaign_video),
    nb_impression BIGINT,
    nb_lancement BIGINT,
    nb_vue BIGINT,
    nb_skip_video BIGINT
);







-- CREATE TABLE campaigns(
--     id BIGSERIAL PRIMARY KEY,
--     name VARCHAR(255),
--     status INT DEFAULT 0,
--     datePublication DATE,
--     id_type_campaign INT NOT NULL REFERENCES type_campaign(id_type_campaign),
--     id_annonceur INT NOT NULL REFERENCES annonceurs(id_annonceur)
-- ); 

-- ALTER TABLE campaigns
--     ADD COLUMN date_modification TIMESTAMP,
--     ADD COLUMN date_debut TIMESTAMP,
--     ADD COLUMN date_fin TIMESTAMP,
--     ADD COLUMN budget DECIMAL,
--     ADD COLUMN vue_max BIGINT,
--     ADD COLUMN id_campaign_video INT NOT NULL REFERENCES campaign_video(id_campaign_video);

-- ALTER TABLE campaigns RENAME COLUMN datePublication TO date_creation;
-- ALTER TABLE campaigns ALTER COLUMN date_creation TYPE TIMESTAMP USING date_creation::TIMESTAMP;

-- ALTER TABLE campaigns ADD CONSTRAINT name_unique UNIQUE(name);


-- CREATE TABLE campaign_video(
--     id_campaign_video BIGSERIAL PRIMARY KEY,
--     name VARCHAR(255),
--     urlVideo TEXT,
--     logo_begin BYTEA,
--     logo_end BYTEA,
--     description TEXT
-- );
-- ALTER TABLE campaign_video
-- ALTER COLUMN logo_begin TYPE VARCHAR(255) USING logo_begin::VARCHAR(255),
-- ALTER COLUMN logo_end TYPE VARCHAR(255) USING logo_end::VARCHAR(255);

-- ALTER TABLE campaign_video
-- ALTER COLUMN logo_begin SET NOT NULL,
-- ALTER COLUMN logo_end SET NOT NULL;





