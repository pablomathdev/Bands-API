package com.github.pablomathdev.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.TrackAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.TrackNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.ITrackRepository;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@Service
public class TrackService {

	@Autowired
	private ITrackRepository trackRepository;

	@Autowired
	private IBandRepository bandRepository;

	@Autowired
	private IGenreRepository genreRepository;

	public List<Track> findAll() {

		return trackRepository.findAll();
	}

	@Transactional
	public void delete(String trackTitle, String bandName) {

		try {
			Track track = trackRepository.findTrackByTitleAndBandName(trackTitle, bandName);
			trackRepository.delete(track);
		} catch (EntityNotFoundException e) {

			throw new TrackNotFoundException(e.getMessage(), e);
		}

	}

	public Track create(Track track) {

		if (trackRepository.exists(track.getTitle(), track.getBand().getName())) {

			throw new TrackAlreadyExistsException(track.getTitle());
		}

		Band band = findBandOrThrow(track.getBand().getName());
		List<Genre> genres = findGenreOrThrow(track.getGenres());

		track.setBand(band);
		track.setGenres(genres);

		return trackRepository.save(track);

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
