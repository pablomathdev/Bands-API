package com.github.pablomathdev.presentation.v1.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.presentation.v1.DTOs.BandRequestDTO;

@Component
public class BandRequestDTOToBand {

	@Autowired
	private ModelMapper modelMapper;

	public Band convert(BandRequestDTO bandRequestDTO) {
	
		TypeMap<BandRequestDTO, Band> bandTypeMap = modelMapper.getTypeMap(BandRequestDTO.class, Band.class);

        if (bandTypeMap == null) {
            Converter<List<String>, List<Genre>> genreConverter = new Converter<List<String>, List<Genre>>() {
                @Override
                public List<Genre> convert(MappingContext<List<String>, List<Genre>> context) {
                    List<String> genreString = context.getSource();
                    return genreString.stream().map(name -> {
                        Genre genre = new Genre();
                        genre.setName(name);
                        return genre;
                    }).collect(Collectors.toList());
                }
            };

            bandTypeMap = modelMapper.createTypeMap(BandRequestDTO.class, Band.class);
            bandTypeMap.addMappings(mapper -> mapper.using(genreConverter).map(BandRequestDTO::getGenres, Band::setGenres));
        }

        return modelMapper.map(bandRequestDTO, Band.class);
	
	}
}
