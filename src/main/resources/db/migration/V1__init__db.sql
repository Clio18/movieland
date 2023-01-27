CREATE TABLE movies(
   id SERIAL PRIMARY KEY,
   name_ukr VARCHAR(100),
   name_eng VARCHAR(100),
   year INTEGER,
   description TEXT,
   rating NUMERIC,
   price NUMERIC,
   picture_path varchar(1000)
);

CREATE TABLE countries(
   id SERIAL PRIMARY KEY,
   name VARCHAR(100)
);

CREATE TABLE genres(
   id SERIAL PRIMARY KEY,
   name VARCHAR(100)
);

CREATE TYPE role AS ENUM ('ADMIN', 'USER');

CREATE TABLE users(
   id SERIAL PRIMARY KEY,
   nick_name VARCHAR(100),
   email VARCHAR(100),
   password VARCHAR(100),
   role role
);

CREATE TABLE reviews(
   id SERIAL PRIMARY KEY,
   movie_id BIGINT,
   user_id BIGINT,
   content TEXT
);

CREATE TABLE movie_country(
   movie_id SERIAL not null,
   FOREIGN KEY (movie_id) REFERENCES movies(id),
   country_id SERIAL not null,
   FOREIGN KEY (country_id) REFERENCES countries(id),
   CONSTRAINT PK_Countries_Movies PRIMARY KEY (movie_id, country_id)
);

CREATE TABLE movie_genre(
   movie_id SERIAL not null,
   FOREIGN KEY (movie_id) REFERENCES movies(id),
   genre_id SERIAL not null,
   FOREIGN KEY (genre_id ) REFERENCES genres(id),
   CONSTRAINT PK_Genres_Movies PRIMARY KEY (movie_id, genre_id)
);

ALTER TABLE movies ALTER COLUMN id SET DEFAULT nextval('movies_id_seq');
ALTER TABLE genres ALTER COLUMN id SET DEFAULT nextval('genres_id_seq');
ALTER TABLE countries ALTER COLUMN id SET DEFAULT nextval('countries_id_seq');
ALTER TABLE reviews ALTER COLUMN id SET DEFAULT nextval('reviews_id_seq');

ALTER SEQUENCE movies_id_seq INCREMENT BY 50;
ALTER SEQUENCE genres_id_seq INCREMENT BY 50;
ALTER SEQUENCE countries_id_seq INCREMENT BY 50;
ALTER SEQUENCE reviews_id_seq INCREMENT BY 50;

ALTER TABLE movie_genre ADD CONSTRAINT genre_id FOREIGN KEY (genre_id) REFERENCES genres (id);
ALTER TABLE movie_genre ADD CONSTRAINT movie_id FOREIGN KEY (movie_id) REFERENCES movies (id);
ALTER TABLE movie_country ADD CONSTRAINT country_id FOREIGN KEY (country_id) REFERENCES countries (id);
ALTER TABLE movie_country ADD CONSTRAINT movie_id FOREIGN KEY (movie_id) REFERENCES movies (id);
ALTER TABLE reviews ADD CONSTRAINT FK_REVIEWS_ON_MOVIE FOREIGN KEY (movie_id) REFERENCES movies (id);
ALTER TABLE reviews ADD CONSTRAINT FK_REVIEWS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

DROP SEQUENCE movie_country_country_id_seq CASCADE;
DROP SEQUENCE movie_country_movie_id_seq CASCADE;
DROP SEQUENCE movie_genre_genre_id_seq CASCADE;
DROP SEQUENCE movie_genre_movie_id_seq CASCADE;