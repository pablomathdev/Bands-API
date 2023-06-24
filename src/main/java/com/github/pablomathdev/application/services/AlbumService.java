package com.github.pablomathdev.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.AlbumAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.EntityNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;
import com.github.pablomathdev.domain.repositories.IBandRepository;
import com.github.pablomathdev.domain.repositories.IGenreRepository;

@Service
public class AlbumService {

	@Autowired
	private IAlbumRepository albumRepository;

	@Autowired
	private IBandRepository bandRepository;

	@Autowired
	private IGenreRepository genreRepository;
	
	
	public List<Album> findAll(){
		
	   return albumRepository.findAll();
	}
	
	public void delete(Album album) {
		albumRepository.delete(album);
	}
	

	public Album create(Album album) {

		if (albumRepository.exists(album.getTitle(), album.getBand().getName())) {

			throw new AlbumAlreadyExistsException(album.getTitle());
		}
		
		Band band = findBandOrThrow(album.getBand().getName());
        List<Genre> genres = findGenreOrThrow(album.getGenres());
        
        album.setBand(band);
        album.setGenres(genres);
        
        return albumRepository.save(album);
	
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
