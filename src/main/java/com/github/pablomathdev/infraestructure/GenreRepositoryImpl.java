package com.github.pablomathdev.infraestructure;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class GenreRepositoryImpl implements IGenreRepository {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Genre save(Genre object) {
		
		entityManager.persist(object);
		
		return object;
		
	}

	@Override
	public Genre findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
