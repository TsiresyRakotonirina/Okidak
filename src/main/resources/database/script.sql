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
    skip boolean,
    temps_skip BIGINT?
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


CREATE TABLE transaction_event(
    id_stat BIGSERIAL PRIMARY KEY,
    date_trans TIMESTAMP,
    id_campaign BIGINT NOT NULL REFERENCES campaigns(id),
    id_campaign_video BIGINT NOT NULL REFERENCES campaign_video(id_campaign_video),
    impression INTEGER,
    lancement INTEGER,
    vue INTEGER,
    skip_video INTEGER,
    quart_lecture INTEGER,
    demi_lecture INTEGER,
    troisquart_lecture INTEGER,
    fin_lecture INTEGER
);





-- -- VIEW 
-- CREATE OR REPLACE VIEW statistique_video AS
-- SELECT 
--     md5(id_campaign::text || '-' || id_campaign_video::text) AS id_stat_unique,
--     DATE(date_trans) AS date,
--     id_campaign, 
--     id_campaign_video, 
--     SUM(impression) AS nb_impression, 
--     SUM(lancement) AS nb_lancement, 
--     SUM(vue) AS nb_vue, 
--     SUM(skip_video) AS nb_skip_video, 
--     SUM(quart_lecture) AS nb_quart_lecture, 
--     SUM(demi_lecture) AS nb_demi_lecture, 
--     SUM(troisquart_lecture) AS nb_troisquart_lecture, 
--     SUM(fin_lecture) AS nb_fin_lecture
-- FROM 
--     transaction_event
-- GROUP BY 
--     DATE(date_trans),
--     id_campaign,
--     id_campaign_video
-- ORDER BY 
--     DATE(date_trans);

-- date_trans,id_campaign,id_campaign_video,impression,lancement,vue,skip_video,quart_lecture,demi_lecture,troisquart_lecture,fin_lecture







