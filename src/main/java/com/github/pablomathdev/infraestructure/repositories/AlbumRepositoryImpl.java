package com.github.pablomathdev.infraestructure.repositories;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class AlbumRepositoryImpl implements IAlbumRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Album> findAll() {
		
	TypedQuery<Album> query = entityManager.createQuery("from Album", Album.class);	
	  return query.getResultList();
		
	}

	@Override
	@Transactional
	public Album save(Album object) {
		
		 entityManager.persist(object);
		 return object;
		
	}

	@Override
	public Album findByName(String title) {
		String jpql = "select a from Album a where a.title = :title";

		TypedQuery<Album> query = entityManager.createQuery(jpql, Album.class);
		query.setParameter("title", title);

		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Album %s not found", title), e);
		}
	}

	@Override
	public boolean exists(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(Album object) {
		// TODO Auto-generated method stub
		
	}
	

}
