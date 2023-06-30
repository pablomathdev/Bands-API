SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM tb_band_genre;
DELETE FROM tb_band;
DELETE FROM tb_genre;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE tb_band AUTO_INCREMENT= 1; 
ALTER TABLE tb_genre AUTO_INCREMENT= 1;


-- some Bands

INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Metallica', 'United States', 'San Francisco', 1981);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Iron Maiden', 'United Kingdom', 'London', 1975);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('AC/DC', 'Australia', 'Sydney', 1973);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Led Zeppelin', 'United Kingdom', 'London', 1968);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Guns N\' Roses', 'United States', 'Los Angeles', 1985);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Queen', 'United Kingdom', 'London', 1970);

-- some genres

INSERT INTO tb_genre (name) VALUES ('Trash Metal');
INSERT INTO tb_genre (name) VALUES ('Heavy Metal');
INSERT INTO tb_genre (name) VALUES ('Hard Rock');

INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,1);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (2,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (3,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (4,3);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (5,3);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (6,3);


INSERT INTO tb_album (band_id, release_date, title) VALUES (1, '1983-07-25', "Kill 'Em All");
INSERT INTO tb_album (band_id, release_date, title) VALUES (1, '1984-07-27', 'Ride the Lightning');
INSERT INTO tb_album (band_id, release_date, title) VALUES (1, '1986-09-08', 'Master of Puppets');


INSERT INTO tb_album_genre (album_id,genre_id) VALUES (1,2);


-- Exemples inserts to musics of album "Master of Puppets"

INSERT INTO tb_track ( album_id, release_date, title) VALUES (3, '1986-03-03', 'Battery');



