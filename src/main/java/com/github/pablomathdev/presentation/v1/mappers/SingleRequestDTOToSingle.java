package com.github.pablomathdev.presentation.v1.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.presentation.v1.DTOs.request.SingleRequestDTO;

@Component
public class SingleRequestDTOToSingle {

	@Autowired
	private ModelMapper modelMapper;

	public Single convert(SingleRequestDTO singleRequestDTO) {
	
		TypeMap<SingleRequestDTO, Single> singleTypeMap = modelMapper.getTypeMap(SingleRequestDTO.class, Single.class);

        if (singleTypeMap == null) {
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

            singleTypeMap = modelMapper.createTypeMap(SingleRequestDTO.class, Single.class);
            singleTypeMap.addMappings(mapper -> {
            	mapper.using(genreConverter).map(SingleRequestDTO::getGenres, Single::setGenres);
                mapper.<String>map(src -> src.getBand(),(Dest,v) -> Dest.getBand().setName(v));
            });
           
        }

        return modelMapper.map(singleRequestDTO, Single.class);
	
	}
}
