INSERT INTO tb_band
(
   name,
   country,
   city,
   formation_year
)
VALUES
(
   'Metallica',
   'United States',
   'San Francisco',
   1981
);




INSERT INTO tb_genre (name) VALUES ('Heavy Metal');
INSERT INTO tb_genre (name) VALUES ('Trash Metal');

INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,1);