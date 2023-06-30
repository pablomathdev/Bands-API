package com.github.pablomathdev.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.TrackAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.AlbumNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.TrackNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;
import com.github.pablomathdev.domain.repositories.ITrackRepository;

@Service
public class TrackService {

	@Autowired
	private ITrackRepository trackRepository;
	
	@Autowired
	private IAlbumRepository albumRepository;

	@Autowired
	private IGenreRepository genreRepository;

	public List<Track> findAll() {

		return trackRepository.findAll();
	}

	@Transactional
	public void delete(String trackTitle, String albumTitle) {

		try {
			Track track = trackRepository.findTrackByTitleAndAlbumTitle(trackTitle, albumTitle);
			trackRepository.delete(track);
		} catch (EntityNotFoundException e) {

			throw new TrackNotFoundException(e.getMessage(), e);
		}

	}

	public Track create(Track track) {

		if (trackRepository.exists(track.getTitle(), track.getAlbum().getBand().getName())) {

			throw new TrackAlreadyExistsException(track.getTitle());
		}

		Album album = findAlbumOrThrow(track.getAlbum().getTitle(),track.getAlbum().getBand().getName());
		List<Genre> genres = findGenreOrThrow(track.getGenres());

		track.setAlbum(album);
		track.setGenres(genres);

		return trackRepository.save(track);

	}

	private Album findAlbumOrThrow(String albumTitle,String bandName) {
		try {
			return albumRepository.findAlbumByTitleAndBandName(albumTitle, bandName);

		} catch (EntityNotFoundException e) {
			throw new AlbumNotFoundException(e.getMessage(), e);

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
