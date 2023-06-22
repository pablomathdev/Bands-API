package com.github.pablomathdev.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.AlbumAlreadyExistsException;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;

@Service
public class AlbumService {

	@Autowired
	private IAlbumRepository albumRepository;

	public Album create(Album album) {

		if (albumRepository.exists(album.getTitle(), album.getBand().getName()) == true) {

			throw new AlbumAlreadyExistsException(album.getTitle());
		}

		return null;

	}

}
