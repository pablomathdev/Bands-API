package com.github.pablomathdev.infraestructure.repositories;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class AlbumRepositoryImpl implements IAlbumRepository{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Album> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Album save(Album object) {
		
		 entityManager.persist(object);
		 return object;
		
	}

	@Override
	public Album findByName(String name) {
		// TODO Auto-generated method stub
		return null;
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
