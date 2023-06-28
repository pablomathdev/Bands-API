package com.github.pablomathdev.infraestructure.repositories;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.ITrackRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class TrackRepositoryImpl implements ITrackRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Track> findAll() {
		
	TypedQuery<Track> query = entityManager.createQuery("from Track", Track.class);	
	  return query.getResultList();
		
	}

	@Override
	@Transactional
	public Track save(Track object) {
		
		 entityManager.persist(object);
		 return object;
		
	}
	@Override
	@Transactional
	public void delete(Track object) {
	     entityManager.remove(object);
	}

	@Override
	public Track findByName(String title) {
		String jpql = "select a from Track a where a.title = :title";

		TypedQuery<Track> query = entityManager.createQuery(jpql, Track.class);
		query.setParameter("title", title);

		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Track %s not found", title), e);
		}
	}
	
	
	public boolean exists(String trackTitle,String bandName) {
		String jpql = "select count(a) from Track a where a.title = :trackTitle AND a.band.name = :bandName";

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("trackTitle", trackTitle);
		query.setParameter("bandName",bandName);

		if (query.getSingleResult() == 1L) {
			return true;
		}

		return false;

	}
	
	public Track findTrackByTitleAndBandName(String trackTitle, String bandName) {
		
		String jpql = "select a from Track a where a.title =:trackTitle AND a.band.name =:bandName";
		TypedQuery<Track> query = entityManager.createQuery(jpql, Track.class);
		query.setParameter("trackTitle",trackTitle);
		query.setParameter("bandName",bandName);
		
		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Track %s of band %s not found", trackTitle,bandName), e);
		}
	}

	
	

}
