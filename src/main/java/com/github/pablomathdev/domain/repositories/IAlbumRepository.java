package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Album;

public interface IAlbumRepository extends IRepository<Album,Integer>{
	boolean exists(String albumTitle,String bandName);
	Album findByName(String title);
	Album findAlbumByTitleAndBandName(String albumTitle, String bandName);
}
