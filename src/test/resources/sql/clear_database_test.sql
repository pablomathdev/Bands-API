SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM tb_band_genre;
DELETE FROM tb_band;
DELETE FROM tb_genre;
DELETE FROM tb_album_genre;
DELETE FROM tb_album;
DELETE FROM tb_track_genre;
DELETE FROM tb_track;
DELETE FROM tb_single;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE tb_band AUTO_INCREMENT= 1; 
ALTER TABLE tb_genre AUTO_INCREMENT= 1;
ALTER TABLE tb_album AUTO_INCREMENT= 1;
ALTER TABLE tb_track AUTO_INCREMENT= 1;