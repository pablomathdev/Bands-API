package com.github.pablomathdev.presentation.v1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.infraestructure.mappers.GenreRequestDTOToGenre;
import com.github.pablomathdev.presentation.v1.DTOs.request.GenreRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/genres")
public class GenreController {

	@Autowired
	private GenreRequestDTOToGenre genreRequestDTOToGenre;

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
	@ResponseStatus(code = HttpStatus.OK)
	public Genre save(@RequestBody Genre genre) {

		return genreService.create(genre);

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid GenreRequestDTO genreRequestDTO) {

		Genre genre = genreService.create(genreRequestDTOToGenre.convert(genreRequestDTO));

		return ResponseEntity.ok(genre);
	}

	@DeleteMapping(value = "/{name}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String name) {

		genreService.delete(name);

	}
}
