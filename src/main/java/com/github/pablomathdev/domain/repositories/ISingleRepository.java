package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Single;

public interface ISingleRepository extends IRepository<Single,Integer>{
	boolean exists(String singleTitle,String bandName);
	Single findByName(String title);
	Single findSingleByTitleAndBandName(String singleTitle, String bandName);
}
