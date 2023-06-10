package com.github.pablomathdev.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;

@Service
public class GenreService implements ICreateService<Genre>{

	@Autowired
	private IGenreRepository genreRepository;
	
	@Override
	public Genre create(Genre object) {
		
		genreRepository.save(object);
		
		return null;
		
	}

}
