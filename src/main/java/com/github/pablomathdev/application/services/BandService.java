package com.github.pablomathdev.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.services.ICreateService;
import com.github.pablomathdev.domain.services.IFindAllService;
import com.github.pablomathdev.infraestructure.mappers.BandUpdateMapper;

import jakarta.persistence.PersistenceException;

@Service
public class BandService implements ICreateService<Band>, IFindAllService<Band> {
	
	@Autowired
	private BandUpdateMapper bandUpdateMapper;

	@Autowired
	private IBandRepository bandRepository;

	@Autowired
	private IGenreRepository genreRepository;

	@Override
	public Band create(Band band) {

		List<Genre> genres = new ArrayList<>();

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

	@Transactional
	public void delete(String nameBand) {

		try {
			Band band = bandRepository.findByName(nameBand);

			bandRepository.delete(band);
		} catch (EntityNotFoundException e) {
			throw new BandNotFoundException(e.getMessage(), e);
		}

	}

	@Transactional
	public Band update(Band band, Integer id) {
		try {
          	Band bandFound = bandRepository.findById(id);

          return bandUpdateMapper.map(band,bandFound);
          	
          	
		} catch (EntityNotFoundException e) {

			throw new BandNotFoundException(e.getMessage(), e);
		}

	}

}