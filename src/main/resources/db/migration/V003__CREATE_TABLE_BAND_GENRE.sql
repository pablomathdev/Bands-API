CREATE TABLE tb_band_genre
(
   band_id BIGINT NOT NULL,
   genre_id BIGINT NOT NULL,
   FOREIGN KEY (band_id) REFERENCES tb_band(id),
   FOREIGN KEY (genre_id) REFERENCES tb_genre(id)
)
ENGINE= InnoDB;
