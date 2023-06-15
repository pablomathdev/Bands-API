package com.github.pablomathdev.application.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;
import com.github.pablomathdev.domain.services.IFindAllService;

import jakarta.persistence.PersistenceException;


@Service
public class BandService implements ICreateService<Band>, IFindAllService<Band> {

	@Autowired
	IBandRepository bandRepository;

	@Autowired
	IGenreRepository genreRepository;

	@Override
	public Band create(Band band) {

		Set<Genre> genres = new HashSet<>();

		if (bandRepository.exists(band.getName()) == true) {

			throw new BandAlreadyExistsException(band.getName());
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

	@Override
	public List<Band> find() {

		return bandRepository.findAll();
	}

}