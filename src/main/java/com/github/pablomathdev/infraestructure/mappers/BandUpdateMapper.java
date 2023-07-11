package com.github.pablomathdev.infraestructure.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Band;

@Component
public class BandUpdateMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Band map(Band src, Band dest) {
		PropertyMap<Band, Band> propertyMap = new PropertyMap<>() {
			protected void configure() {
				skip(destination.getId()); // Exclude the 'id' field from mapping
			}
		};

		modelMapper.addMappings(propertyMap);

		modelMapper.map(src, dest);

		return dest;
	}

}
