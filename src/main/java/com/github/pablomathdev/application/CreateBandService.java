package com.github.pablomathdev.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IRepository;
import com.github.pablomathdev.domain.services.ICreateService;

@Service
public class CreateBandService implements ICreateService<Band> {
    
	@Autowired
	IRepository<Genre, Integer> genreRepository;
	
	@Autowired
	IRepository<Band,Integer> bandRepository;

	@Override
	public Band execute(Band entity) {
	  
					
	 return bandRepository.save(entity);
	
	}

}
