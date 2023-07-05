package com.github.pablomathdev.infraestructure.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.presentation.v1.DTOs.request.AlbumRequestDTO;

@Component
public class AlbumRequestDTOToAlbum {

	@Autowired
	private ModelMapper modelMapper;

	public Album convert(AlbumRequestDTO albumRequestDTO) {
	
		TypeMap<AlbumRequestDTO, Album> albumTypeMap = modelMapper.getTypeMap(AlbumRequestDTO.class, Album.class);

        if (albumTypeMap == null) {
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

            albumTypeMap = modelMapper.createTypeMap(AlbumRequestDTO.class, Album.class);
            albumTypeMap.addMappings(mapper -> {
            	mapper.using(genreConverter).map(AlbumRequestDTO::getGenres, Album::setGenres);
                mapper.<String>map(src -> src.getBand(),(Dest,v) -> Dest.getBand().setName(v));
            });
           
        }

        return modelMapper.map(albumRequestDTO, Album.class);
	
	}
}
