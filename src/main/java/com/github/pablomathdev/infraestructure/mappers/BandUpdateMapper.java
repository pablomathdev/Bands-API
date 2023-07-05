package com.github.pablomathdev.infraestructure.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Band;

@Component
public class BandUpdateMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Band map(Band band) {

		return modelMapper.map(band, Band.class);
	}

}
