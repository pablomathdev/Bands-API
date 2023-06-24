package com.github.pablomathdev.domain.repositories;

import com.github.pablomathdev.domain.entities.Genre;

public interface IGenreRepository extends IRepository<Genre, Integer> {
	boolean exists(String name);
	Genre findByName(String name);
}
