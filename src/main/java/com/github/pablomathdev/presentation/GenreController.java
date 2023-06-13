package com.github.pablomathdev.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;

@RestController
@RequestMapping(value = "/api/genres")
public class GenreController {

	
	@Autowired
	private GenreService genreService;
	
	
	@PostMapping
	public ResponseEntity<?> save(@RequestBody Genre genre){
		
		Genre genreSaved = genreService.create(genre);
		
		return ResponseEntity.ok(genreSaved);
	}
}
