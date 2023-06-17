package com.github.pablomathdev.presentation.v1.controllers;

import static com.github.pablomathdev.presentation.v1.utils.TransformeString.tranform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.exceptions.EntityRelationshipException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.GenreAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;

@RestController
@RequestMapping(value = "/v1/genres")
public class GenreController {

	@Autowired
	private GenreService genreService;

	@GetMapping
	public ResponseEntity<List<Genre>> findAllBands() {

		List<Genre> genres = genreService.find();

		if (genres.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genres);
		}

		return ResponseEntity.ok(genres);

	}

	@PostMapping
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

	@DeleteMapping(value = "/{name}")
	public ResponseEntity<?> delete(@PathVariable String name) {

		try {

			genreService.delete(tranform(name));
			return ResponseEntity.ok().build();
		} catch (GenreNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch (EntityRelationshipException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}

	}
}