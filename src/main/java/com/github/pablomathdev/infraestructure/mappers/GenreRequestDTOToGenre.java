package com.github.pablomathdev.infraestructure.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.presentation.v1.DTOs.request.GenreRequestDTO;

@Component
public class GenreRequestDTOToGenre {

	@Autowired
	private ModelMapper modelMapper;

	public Genre convert(GenreRequestDTO genreRequestDTO) {
	
		return modelMapper.map(genreRequestDTO, Genre.class);
		
	}
}
