package com.github.pablomathdev.infraestructure.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Album;

@Component
public class AlbumUpdateMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Album map(Album src, Album dest) {

		TypeMap<Album, Album> AlbumTypeMap = modelMapper.getTypeMap(Album.class, Album.class);

		PropertyMap<Album, Album> propertyMap = new PropertyMap<>() {
			protected void configure() {
				skip(destination.getId()); // Exclude the 'id' field from mapping
			}
		};

		if (AlbumTypeMap == null) {
			modelMapper.addMappings(propertyMap);
		}

		modelMapper.map(src, dest);

		return dest;

	}

}
