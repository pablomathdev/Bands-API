package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Track;

public interface ITrackRepository extends IRepository<Track,Integer>{
	boolean exists(String trackTitle,String bandName);
	Track findByName(String title);
	Track findTrackByTitleAndBandName(String trackTitle, String bandName);
}
