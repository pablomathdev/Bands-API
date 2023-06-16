package com.github.pablomathdev.infraestructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class GenreRepositoryImpl implements IGenreRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Genre save(Genre object) {

		entityManager.persist(object);
		return object;

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
			throw new EntityNotFoundException(String.format("Genre %s Not Found!", name));
		}

	}

	@Override
	public List<Genre> findAll() {
	    TypedQuery<Genre> query = entityManager.createQuery("from Genre",Genre.class);
	    
	    return query.getResultList();
	}

	@Override
	public boolean exists(String name) {
		String jpql = "select count(g) from Genre g where g.name = :name";

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("name", name);

		if (query.getSingleResult() == 1L) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void delete(Genre object) {
	
		entityManager.remove(object);
		entityManager.flush();
		
	}

}
