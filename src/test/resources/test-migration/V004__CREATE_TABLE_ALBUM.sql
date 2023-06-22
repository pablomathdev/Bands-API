CREATE TABLE tb_album
(
   id BIGINT AUTO_INCREMENT NOT NULL,
   title VARCHAR (50) NOT NULL,
   band_id BIGINT NOT NULL,
   release_date DATE NOT NULL,
   FOREIGN KEY (band_id) REFERENCES tb_band (id),
   PRIMARY KEY (id)
)
ENGINE= InnoDB;