SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM tb_band_genre;
DELETE FROM tb_band;
DELETE FROM tb_genre;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE tb_band AUTO_INCREMENT= 1; 
ALTER TABLE tb_genre AUTO_INCREMENT= 1;