CREATE TABLE tb_track
(
   id BIGINT AUTO_INCREMENT NOT NULL,
   title VARCHAR (50) NOT NULL,
   album_id BIGINT,
   single_id BIGINT,
   release_date DATE NOT NULL,
   FOREIGN KEY (album_id) REFERENCES tb_album (id) ON DELETE CASCADE,
   FOREIGN KEY (single_id) REFERENCES tb_single (id),
   PRIMARY KEY (id)
)
ENGINE= InnoDB;