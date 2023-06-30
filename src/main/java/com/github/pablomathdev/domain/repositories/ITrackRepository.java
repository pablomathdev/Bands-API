package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Track;

public interface ITrackRepository extends IRepository<Track,Integer>{
	boolean exists(String trackTitle,String albumTitle);
	Track findByName(String title);
	Track findTrackByTitleAndAlbumTitle(String trackTitle, String bandName);
}
