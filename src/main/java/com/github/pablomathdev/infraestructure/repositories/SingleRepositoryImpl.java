package com.github.pablomathdev.infraestructure.repositories;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.repositories.ISingleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class SingleRepositoryImpl implements ISingleRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Single> findAll() {
		
	TypedQuery<Single> query = entityManager.createQuery("from Single", Single.class);	
	  return query.getResultList();
		
	}

	@Override
	@Transactional
	public Single save(Single object) {
		
		 entityManager.persist(object);
		 return object;
		
	}
	@Override
	@Transactional
	public void delete(Single object) {
	     entityManager.remove(object);
	}

	@Override
	public Single findByName(String title) {
		String jpql = "select s from Single s where s.title = :title";

		TypedQuery<Single> query = entityManager.createQuery(jpql, Single.class);
		query.setParameter("title", title);

		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Single %s not found", title), e);
		}
	}
	
	
	public boolean exists(String singleTitle,String bandName) {
		String jpql = "select count(s) from Single s where s.title = :singleTitle AND s.band.name = :bandName";

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("singleTitle", singleTitle);
		query.setParameter("bandName",bandName);

		if (query.getSingleResult() == 1L) {
			return true;
		}

		return false;

	}
	
	public Single findSingleByTitleAndBandName(String singleTitle, String bandName) {
		
		String jpql = "select s from Single s where s.title =:singleTitle AND s.band.name =:bandName";
		TypedQuery<Single> query = entityManager.createQuery(jpql, Single.class);
		query.setParameter("singleTitle",singleTitle);
		query.setParameter("bandName",bandName);
		
		try {
			return query.getSingleResult();
		}catch (NoResultException e) {
			throw new EntityNotFoundException(String.format("Single %s of band %s not found", singleTitle,bandName), e);
		}
	}

	@Override
	public Single update(Single object) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
