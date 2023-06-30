CREATE TABLE tb_track_genre
(
   track_id BIGINT NOT NULL,
   genre_id BIGINT NOT NULL,
   FOREIGN KEY (track_id) REFERENCES tb_track(id),
   FOREIGN KEY (genre_id) REFERENCES tb_genre(id)
)
ENGINE= InnoDB;
