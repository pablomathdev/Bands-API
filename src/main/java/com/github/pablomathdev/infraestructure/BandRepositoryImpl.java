package com.github.pablomathdev.infraestructure;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.repositories.IRepository;

@Repository
public class BandRepositoryImpl implements IRepository<Band,Integer>{

	@Override
	public Band save(Band band) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Band findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
