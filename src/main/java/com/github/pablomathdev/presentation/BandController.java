package com.github.pablomathdev.presentation;

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

import com.github.pablomathdev.application.services.BandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.GenreNotFoundException;

@RestController
@RequestMapping(value = "/api")
public class BandController {

	@Autowired
	private BandService bandService;

	@GetMapping("/bands")
	public ResponseEntity<List<Band>> findAllBands() {

		List<Band> bands = bandService.find();

		if (bands.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bands);
		}

		return ResponseEntity.ok(bands);

	}

	@PostMapping("/bands")
	public ResponseEntity<?> save(@RequestBody Band band) {

		try {

			Band bandSaved = bandService.create(band);
			return ResponseEntity.status(HttpStatus.CREATED).body(bandSaved);
		} catch (BandAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (EntitySaveException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (GenreNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
	@DeleteMapping(value = "/bands/{name}")
	public ResponseEntity<?> delete(@PathVariable String name){
		
		try {
			bandService.delete(name);
			return ResponseEntity.ok().build();
		}catch (BandNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}

}
