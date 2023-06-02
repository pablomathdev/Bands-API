package com.github.pablomathdev.infraestructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@Repository
public class GenreRepositoryImpl implements IGenreRepository {

	@Override
	public Genre save(Genre object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Genre> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
