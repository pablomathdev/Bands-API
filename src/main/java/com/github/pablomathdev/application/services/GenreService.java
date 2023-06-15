package com.github.pablomathdev.application.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;
import com.github.pablomathdev.domain.services.IFindAllService;

import jakarta.persistence.PersistenceException;

@Service
public class GenreService implements ICreateService<Genre>,IFindAllService<Genre> {

	@Autowired
	private IGenreRepository genreRepository;

	@Override
	public Genre create(Genre genre) {

		if(genreRepository.exists(genre.getName()) == true) {
			throw new GenreAlreadyExistsException(genre.getName());
		}
		
		
		try {
			return genreRepository.save(genre);
		} catch (PersistenceException e) {
			throw new EntitySaveException(String.format("Failed to save the genre %s", genre.getName()), e);
		}

	}
	
	@Override
	public List<Genre> find() {

		return genreRepository.findAll();
	}

}
