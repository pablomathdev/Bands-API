package com.github.pablomathdev.infraestructure;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class BandRepositoryImpl implements IBandRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Band save(Band object) {

		entityManager.persist(object);
		return object;

	}

	@Override
	public Band findByName(String name) {

		String jpql = "select b from Band b where b.name = :name";

		TypedQuery<Band> query = entityManager.createQuery(jpql, Band.class);
		query.setParameter("name", name);

		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Band %s not found!", name), e);
		}
		
	}

	public boolean exists(String name) {
		String jpql = "select count(b) from Band b where b.name = :name";

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("name", name);

		if (query.getSingleResult() == 1L) {
			return true;
		}

		return false;

	}

	@Override
	public List<Band> findAll() {
	
		TypedQuery<Band> query = entityManager.createQuery("from Band", Band.class);
		
		return query.getResultList();
	}

	@Override
	@Transactional
	public void delete(Band band) {
	
		entityManager.remove(band);
	}

}
