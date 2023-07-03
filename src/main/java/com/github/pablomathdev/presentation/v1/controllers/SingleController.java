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

import com.github.pablomathdev.application.services.SingleService;
import com.github.pablomathdev.domain.entities.Single;

@RestController
@RequestMapping(value = "/v1/singles")
public class SingleController {
//	
//	@Autowired
//	private AlbumRequestDTOToAlbum albumRequestDTOToAlbum;

	@Autowired
	private SingleService singleService;
	
	@GetMapping
	public ResponseEntity<List<Single>> findAllSingles(){
		
	   List<Single> singles = singleService.findAll();
	   
	   if(singles.isEmpty()) {
		   return ResponseEntity.status(HttpStatus.NO_CONTENT).body(singles);
	   }
	   
	   return ResponseEntity.ok(singles);
	   
	}
	

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Single single) {
        
	  
		
		
		Single singleSaved = singleService.create(single);

		return ResponseEntity.status(HttpStatus.CREATED).body(singleSaved);

	}
	
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam String singleTitle,@RequestParam String bandName) {
		
		singleService.delete(singleTitle, bandName);
	}
	
	
}
