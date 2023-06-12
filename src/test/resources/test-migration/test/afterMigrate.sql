
set foreign_key_checks = 0;

DELETE FROM tb_band;
DELETE FROM tb_genre;
DELETE FROM tb_band_genre;

set foreign_key_checks = 1;

ALTER TABLE tb_band AUTO_INCREMENT=1;
ALTER TABLE tb_genre AUTO_INCREMENT=1;
ALTER TABLE tb_band_genre AUTO_INCREMENT=1;

-- Bands

INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Metallica', 'United States', 'San Francisco', 1981);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Iron Maiden', 'United Kingdom', 'London', 1975);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('AC/DC', 'Australia', 'Sydney', 1973);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Led Zeppelin', 'United Kingdom', 'London', 1968);


-- some genres

INSERT INTO tb_genre (name) VALUES ('Trash Metal');
INSERT INTO tb_genre (name) VALUES ('Heavy Metal');
INSERT INTO tb_genre (name) VALUES ('Hard Rock');

INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,1);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (2,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (3,2);
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (4,3);


