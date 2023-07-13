package com.github.pablomathdev.application.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntityRelationshipException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
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
	
	
	
	public Genre update(Genre genre,Integer id) {
	
		try {
			Genre genreFound = genreRepository.findById(id);
			
			genreFound.setName(genre.getName());
			
			
			return genreRepository.save(genreFound);
			
			
		}catch (EntityNotFoundException e) {
		    throw new EntityNotFoundException(e.getMessage(),e);
		}
		
		
	
		
	}
	
	@Override
	public List<Genre> find() {

		return genreRepository.findAll();
	}
	
	@Transactional
	public void delete(String nameGenre) {
		
		try {
			Genre genre = genreRepository.findByName(nameGenre);
			
			genreRepository.delete(genre);
		}catch (EntityNotFoundException e) {
			throw new GenreNotFoundException(e.getMessage(),e);
		}catch (DataIntegrityViolationException e) {
			throw new EntityRelationshipException(String.format("Cannot delete genre '%s'", nameGenre),e);
		}
		
		
	}

}
