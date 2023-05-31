package com.github.pablomathdev.infraestructure;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IRepository;

@Repository
public class GenreRepositoryImpl implements IRepository<Genre, Integer> {

	@Override
	public Genre save(Genre object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Genre findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
