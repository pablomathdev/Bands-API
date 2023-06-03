package com.github.pablomathdev.infraestructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IFindableRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class BandRepositoryImpl implements IBandRepository, IFindableRepository<Band, Integer> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Band save(Band object) {

		try {
			entityManager.persist(object);
			entityManager.flush();

			return findByName(object.getName()).orElse(object);
		} catch (EntityExistsException e) {
			throw new BandAlreadyExistsException();
		}

	}

	@Override
	public Optional<Band> findByName(String name) {

		String jpql = "select b from Band b where b.nome = :nome";

		TypedQuery<Band> query = entityManager.createQuery(jpql, Band.class);
		query.setParameter("name", name);

		try {
			Band result = query.getSingleResult();

			return Optional.ofNullable(result);
		} catch (NoResultException e) {

			throw new EntityNotFoundException(String.format("Entity with name %s not found!", name));
		}

	}

	@Override
	public Optional<Band> findById(Integer id) {

		Band band = entityManager.find(Band.class, id);
		Optional<Band> optionalBand = Optional.ofNullable(band);

		return optionalBand;
	}
}
