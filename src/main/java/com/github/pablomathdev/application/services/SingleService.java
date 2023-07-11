package com.github.pablomathdev.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.SingleAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.SingleNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.ISingleRepository;

@Service
public class SingleService {

	@Autowired
	private IBandRepository bandRepository;

	@Autowired
	private ISingleRepository singleRepository;

	@Autowired
	private IGenreRepository genreRepository;

	public List<Single> findAll() {

		return singleRepository.findAll();
	}

	@Transactional
	public void delete(String singleTitle, String bandName) {

		try {
			Single single = singleRepository.findSingleByTitleAndBandName(singleTitle, bandName);
			singleRepository.delete(single);
		} catch (EntityNotFoundException e) {

			throw new SingleNotFoundException(e.getMessage(), e);
		}

	}

	@Transactional
	public Single create(Single single) {

		if (singleRepository.exists(single.getTitle(), single.getBand().getName())) {

			throw new SingleAlreadyExistsException(single.getTitle());
		}

		Band band = findBandOrThrow(single.getBand().getName());
		List<Genre> genres = findGenreOrThrow(single.getGenres());

		single.setBand(band);
		single.setGenres(genres);

		return singleRepository.save(single);

	}

	private Band findBandOrThrow(String bandName) {
		try {
			return bandRepository.findByName(bandName);

		} catch (EntityNotFoundException e) {
			throw new BandNotFoundException(e.getMessage(), e);

		}
	}

	private List<Genre> findGenreOrThrow(List<Genre> genresList) {

		List<Genre> genres = new ArrayList<>();
		try {

			genresList.forEach((g) -> {

				Genre genre = genreRepository.findByName(g.getName());

				genres.add(genre);

			});

			return genres;

		} catch (EntityNotFoundException e) {
			throw new GenreNotFoundException(e.getMessage(), e);

		}
	}

}
