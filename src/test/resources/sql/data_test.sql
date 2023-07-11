-- Bands --
INSERT INTO tb_band(name,country,city,formation_year)VALUES('Metallica','United States','San Francisco',1981);

-- Albums --
INSERT INTO tb_album(band_id,release_date,title)VALUES(1,'1991-08-12','Metallica (The Black Album)');


-- Genres --
INSERT INTO tb_genre (name) VALUES ('Heavy Metal');
INSERT INTO tb_genre (name) VALUES ('Trash Metal');

 -- Band_Genre --
INSERT INTO tb_band_genre (band_id,genre_id) VALUES (1,1);

-- Album_Genre -- 
INSERT INTO tb_album_genre (album_id,genre_id) VALUES(1,1);

-- Track --
INSERT INTO tb_track (album_id, release_date, title) VALUES (1, '1991-07-29', 'Enter Sandman');

-- Track_Genre
INSERT INTO tb_track_genre (track_id,genre_id) VALUES (1,1);

-- Single
INSERT INTO tb_single(band_id,release_date,title)VALUES(1,'1983-11-08','Whiplash');

-- Album_Genre -- 
INSERT INTO tb_single_genre (single_id,genre_id) VALUES(1,2);


