package com.github.pablomathdev.presentation.v1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.AlbumService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.presentation.v1.DTOs.AlbumRequestDTO;
import com.github.pablomathdev.presentation.v1.mappers.AlbumRequestDTOToAlbum;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/albums")
public class AlbumController {
	
	@Autowired
	private AlbumRequestDTOToAlbum albumRequestDTOToAlbum;

	@Autowired
	private AlbumService albumService;
	
	@GetMapping
	public ResponseEntity<List<Album>> findAllAlbums(){
		
	   List<Album> albums = albumService.findAll();
	   
	   if(albums.isEmpty()) {
		   return ResponseEntity.status(HttpStatus.NO_CONTENT).body(albums);
	   }
	   
	   return ResponseEntity.ok(albums);
	   
	}
	

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid AlbumRequestDTO albumRequestDTO) {
        
	     Album album = albumRequestDTOToAlbum.convert(albumRequestDTO);
		
		
		Album albumSaved = albumService.create(album);

		return ResponseEntity.status(HttpStatus.CREATED).body(albumSaved);

	}
	
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam String albumTitle,@RequestParam String bandName) {
		
		albumService.delete(albumTitle, bandName);
	}
	
	
}
