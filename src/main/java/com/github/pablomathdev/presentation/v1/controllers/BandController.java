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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/bands")
public class BandController {

	@Autowired
	private BandService bandService;

	@GetMapping
	public ResponseEntity<List<Band>> findAllBands() {

		List<Band> bands = bandService.find();

		if (bands.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bands);
		}

		return ResponseEntity.ok(bands);

	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid Band band) {

		try {

			Band bandSaved = bandService.create(band);
			return ResponseEntity.status(HttpStatus.CREATED).body(bandSaved);
		}catch (EntitySaveException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} 

	}

	@DeleteMapping(value = "/{name}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String name) {
  
	  bandService.delete(tranform(name));

	}

}
