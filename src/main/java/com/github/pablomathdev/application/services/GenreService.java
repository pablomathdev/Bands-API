package com.github.pablomathdev.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;

import jakarta.persistence.PersistenceException;

@Service
public class GenreService implements ICreateService<Genre> {

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

}
