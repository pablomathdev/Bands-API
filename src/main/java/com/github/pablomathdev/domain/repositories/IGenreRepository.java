package com.github.pablomathdev.domain.repositories;

import java.util.Optional;

import com.github.pablomathdev.domain.entities.Genre;

public interface IGenreRepository {

	Optional<Genre> findByName(String name);
	
	
}
