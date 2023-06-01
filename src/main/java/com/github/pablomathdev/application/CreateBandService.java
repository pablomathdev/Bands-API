package com.github.pablomathdev.application;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.IRepository;
import com.github.pablomathdev.domain.services.ICreateService;

@Service
public class CreateBandService implements ICreateService<Band> {

	@Autowired
	IGenreRepository genreRepository;

	@Autowired
	IRepository<Band, Integer> bandRepository;

	@Override
	public Band execute(Band entity) {

		Set<Genre> genres = new HashSet<>();

		entity.getGenres().stream()
		.map(Genre::getName).forEach((name) -> {
			Optional<Genre> genre = genreRepository.findByName(name);

			genre.ifPresent(genres::add);

		});

		entity.setGenres(genres);

		return bandRepository.save(entity);

	}

}
