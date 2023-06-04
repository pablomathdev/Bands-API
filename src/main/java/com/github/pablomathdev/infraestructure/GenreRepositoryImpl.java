package com.github.pablomathdev.infraestructure;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class GenreRepositoryImpl implements IGenreRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Genre save(Genre object) {

		if (findByName(object.getName()) == null) {

		}

		entityManager.persist(object);

		return object;

	}

	@Override
	public Genre findByName(String name) {

		String jpql = "select g from Genre g where g.name = :name";

		TypedQuery<Genre> query = entityManager.createQuery(jpql, Genre.class);
        query.setParameter("name", name);
		
        try {
			return query.getSingleResult();
		} catch (NoResultException e) {

			return null;
		}

	}

}
