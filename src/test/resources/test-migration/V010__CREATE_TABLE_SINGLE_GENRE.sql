CREATE TABLE tb_single_genre
(
   single_id BIGINT NOT NULL,
   genre_id BIGINT NOT NULL,
   FOREIGN KEY (single_id) REFERENCES tb_single(id) ON DELETE CASCADE,
   FOREIGN KEY (genre_id) REFERENCES tb_genre(id)
)
ENGINE= InnoDB;
