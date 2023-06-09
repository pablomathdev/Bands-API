CREATE TABLE tb_band
(
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR (50) NOT NULL,
   country VARCHAR (20) NOT NULL,
   city VARCHAR (20) NOT NULL,
   formation_year INT NOT NULL,
   PRIMARY KEY (id)
)
ENGINE= InnoDB;