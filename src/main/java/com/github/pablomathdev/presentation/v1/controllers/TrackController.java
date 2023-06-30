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

import com.github.pablomathdev.application.services.TrackService;
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.presentation.v1.DTOs.TrackRequestDTO;
import com.github.pablomathdev.presentation.v1.mappers.TrackRequestDTOToTrack;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/tracks")
public class TrackController {
	
	@Autowired
	private TrackRequestDTOToTrack trackRequestDTOToTrack;

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
	

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid TrackRequestDTO trackRequestDTO) {
        
		Track track = trackRequestDTOToTrack.convert(trackRequestDTO);
			
		Track trackSaved = trackService.create(track);

		return ResponseEntity.status(HttpStatus.CREATED).body(trackSaved);

	}
	
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam String trackTitle,@RequestParam String albumTitle) {
		
		trackService.delete(trackTitle, albumTitle);
	}
	
	
}
