INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Metallica', 'United States', 'San Francisco', 1981);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Iron Maiden', 'United Kingdom', 'London', 1975);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('AC/DC', 'Australia', 'Sydney', 1973);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Led Zeppelin', 'United Kingdom', 'London', 1968);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Guns N\' Roses', 'United States', 'Los Angeles', 1985);
INSERT INTO tb_band (name, country, city, formation_year) VALUES ('Queen', 'United Kingdom', 'London', 1970);



INSERT INTO tb_genre (name) VALUES ('Trash Metal');
INSERT INTO tb_genre (name) VALUES ('Heavy Metal');
INSERT INTO tb_genre (name) VALUES ('Hard Rock');


INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (1,1);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (1,2);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (2,2);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (3,2);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (4,3);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (5,3);
INSERT INTO tb_band_genre (tb_band_id,tb_genre_id) VALUES (6,3);