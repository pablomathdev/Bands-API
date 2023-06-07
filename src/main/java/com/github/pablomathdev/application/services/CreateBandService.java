package com.github.pablomathdev.application.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;

import jakarta.persistence.PersistenceException;
import lombok.Setter;

@Setter
@Service
public class CreateBandService implements ICreateService<Band> {

	@Autowired
	IBandRepository bandRepository;

	@Autowired
	IGenreRepository genreRepository;

	@Override
	public Band execute(Band band) {

		Set<Genre> genres = new HashSet<>();

		if (bandRepository.exists(band.getName()) == true) {

			throw new BandAlreadyExistsException();
		}

		try {

			band.getGenres().forEach((g) -> {

				Genre genre = genreRepository.findByName(g.getName());
				genres.add(genre);

			});
			
			band.setGenres(genres);			
			return bandRepository.save(band);

		} catch (EntityNotFoundException e) {
			throw new GenreNotFoundException(e.getMessage(), e);
		} catch (PersistenceException e) {
			throw new EntitySaveException(String.format("Failed to save the band %s", band.getName()), e);
		}

	}

}