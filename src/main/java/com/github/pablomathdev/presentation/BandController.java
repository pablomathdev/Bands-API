package com.github.pablomathdev.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pablomathdev.application.services.CreateBandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.exceptions.BandAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.EntitySaveException;
import com.github.pablomathdev.domain.exceptions.GenreNotFoundException;
import com.github.pablomathdev.domain.repositories.IBandRepository;

@RestController
@RequestMapping(value = "/api")
public class BandController {

	@Autowired
	private CreateBandService createBandService;

	@Autowired
	private IBandRepository bandRepository;
	
	@GetMapping("/bands")
	public List<Band> findAll(){
		
		return bandRepository.findAll();
		
	}
	
	
	@PostMapping("/bands")
	public ResponseEntity<?> save(@RequestBody Band band) {

		try {

			Band bandSaved = createBandService.execute(band);
			return ResponseEntity.status(HttpStatus.CREATED).body(bandSaved);
		} catch (BandAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (EntitySaveException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (GenreNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}
	


}
