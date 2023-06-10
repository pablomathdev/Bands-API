package com.github.pablomathdev.infraestructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class GenreRepositoryImpl implements IGenreRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Genre save(Genre object) {
		try {

			entityManager.persist(object);
			return object;

		} catch (PersistenceException e) {
			throw new EntitySaveException(String.format("Failed to save the genre %s", object.getName()), e);
		}

	}

	@Override
	public Genre findByName(String name) {

		String jpql = "select g from Genre g where g.name = :name";

		TypedQuery<Genre> query = entityManager.createQuery(jpql, Genre.class);
		query.setParameter("name", name);

		List<Genre> result = query.getResultList();

		if (!result.isEmpty()) {
			return result.get(0);
		} else {
			throw new EntityNotFoundException(String.format("Genre %s Not Found!",name));
		}

	}

	@Override
	public List<Genre> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
