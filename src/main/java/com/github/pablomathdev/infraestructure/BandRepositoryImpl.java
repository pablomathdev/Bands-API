package com.github.pablomathdev.infraestructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IFindableRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class BandRepositoryImpl implements IBandRepository, IFindableRepository<Band, Integer> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Band save(Band object) {

		try {
			entityManager.persist(object);
			entityManager.flush();

			return object;
		} catch (EntityExistsException e) {
			throw new BandAlreadyExistsException();
		}

	}

	@Override
	public Optional<Band> findByName(String name) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Band> findById(Integer id) {

		Band band = entityManager.find(Band.class, id);
	    Optional<Band> optionalBand = Optional.ofNullable(band);
	   
	    return optionalBand;
	}
}
