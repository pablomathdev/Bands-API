CREATE TABLE tb_album_genre
(
   album_id BIGINT NOT NULL,
   genre_id BIGINT NOT NULL,
   FOREIGN KEY (album_id) REFERENCES tb_album(id),
   FOREIGN KEY (genre_id) REFERENCES tb_genre(id)
)
ENGINE= InnoDB;
