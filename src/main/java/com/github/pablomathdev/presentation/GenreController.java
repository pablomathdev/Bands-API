package com.github.pablomathdev.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;

@RestController
@RequestMapping(value = "/api")
public class GenreController {


	@Autowired 
	private GenreService genreService;
	
	

	@PostMapping(value = "/genres")
	public ResponseEntity<?> save(@RequestBody Genre genre) {

		try {
			Genre genreSaved = genreService.create(genre);
			return ResponseEntity.ok(genreSaved);
		} catch (GenreAlreadyExistsException e) {

			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (EntitySaveException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}
}
