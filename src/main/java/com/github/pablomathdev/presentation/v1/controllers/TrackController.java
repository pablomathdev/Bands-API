package com.github.pablomathdev.presentation.v1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.TrackService;
import com.github.pablomathdev.domain.entities.Track;

@RestController
@RequestMapping(value = "/v1/tracks")
public class TrackController {
	
//	@Autowired
//	private AlbumRequestDTOToAlbum albumRequestDTOToAlbum;

	@Autowired
	private TrackService trackService;
	
	@GetMapping
	public ResponseEntity<List<Track>> findAllTracks(){
		
	   List<Track> tracks = trackService.findAll();
	   
	   if(tracks.isEmpty()) {
		   return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tracks);
	   }
	   
	   return ResponseEntity.ok(tracks);
	   
	}
	
//
//	@PostMapping
//	public ResponseEntity<?> save(@RequestBody @Valid AlbumRequestDTO albumRequestDTO) {
//        
//	     Album album = albumRequestDTOToAlbum.convert(albumRequestDTO);
//		
//		
//		Album albumSaved = albumService.create(album);
//
//		return ResponseEntity.status(HttpStatus.CREATED).body(albumSaved);
//
//	}
//	
//	@DeleteMapping
//	@ResponseStatus(value = HttpStatus.NO_CONTENT)
//	public void delete(@RequestParam String albumTitle,@RequestParam String bandName) {
//		
//		albumService.delete(albumTitle, bandName);
//	}
//	
//	
}
