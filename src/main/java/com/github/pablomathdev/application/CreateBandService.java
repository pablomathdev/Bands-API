package com.github.pablomathdev.application;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;

@Service
public class CreateBandService implements ICreateService<Band> {

	@Autowired
	IBandRepository bandRepository;

	@Autowired
	IGenreRepository genreRepository;

	@Override
	public Band execute(Band band) {

		Set<Genre> genres = new HashSet<>();
		
		band.getGenres().forEach((g) -> {

			Optional<Genre> genre = genreRepository.findByName(g.getName());
			
			if(genre.isPresent()) {
				genres.add(genre.get());
			}
		});

		Optional<Band> band1 = bandRepository.save(band);

		return band1.orElse(null);
	}

}