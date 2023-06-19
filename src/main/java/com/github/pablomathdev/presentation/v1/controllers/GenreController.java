package com.github.pablomathdev.presentation.v1.controllers;

import static com.github.pablomathdev.presentation.v1.utils.TransformeString.tranform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.GenreService;
import com.github.pablomathdev.domain.entities.Genre;

@RestController
@RequestMapping(value = "/v1/genres")
public class GenreController {

	@Autowired
	private GenreService genreService;

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<Genre> findAllBands() {

		return genreService.find();

	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	public Genre save(@RequestBody Genre genre) {

		return genreService.create(genre);

	}

	@DeleteMapping(value = "/{name}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String name) {

		genreService.delete(tranform(name));

	}
}
