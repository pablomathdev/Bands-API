package com.github.pablomathdev.infraestructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.AlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IFindableRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

@Repository
public class BandRepositoryImpl implements IBandRepository, IFindableRepository<Band, Integer> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Band save(Band object) {

		try {
			if(findByName(object.getName()) != null) {
				throw new AlreadyExistsException();
			};

			entityManager.persist(object);
			return object;

		} catch (PersistenceException e) {

			throw new EntitySaveException(String.format("Failed to save the band %s", object.getName()), e);
		} 

	}

	@Override
	public Band findByName(String name) {

		String jpql = "select b from Band b where b.name = :name";

		TypedQuery<Band> query = entityManager.createQuery(jpql, Band.class);
		query.setParameter("name", name);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	@Override
	public Optional<Band> findById(Integer id) {

		Band band = entityManager.find(Band.class, id);
		Optional<Band> optionalBand = Optional.ofNullable(band);

		return optionalBand;
	}
}
