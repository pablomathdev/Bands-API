package com.github.pablomathdev.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.repositories.IAlbumRepository;

@Service
public class AlbumService {

	@Autowired
	private IAlbumRepository albumRepository;

	public Album create(Album album) {

		albumRepository.exists(album.getTitle(), album.getBand().getName());
		return null;

	}

}
