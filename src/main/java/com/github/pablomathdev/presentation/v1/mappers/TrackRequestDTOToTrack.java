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
import com.github.pablomathdev.domain.entities.Track;
import com.github.pablomathdev.presentation.v1.DTOs.TrackRequestDTO;

@Component
public class TrackRequestDTOToTrack {

	@Autowired
	private ModelMapper modelMapper;

	public Track convert(TrackRequestDTO trackRequestDTO) {

		TypeMap<TrackRequestDTO, Track> trackTypeMap = modelMapper.getTypeMap(TrackRequestDTO.class, Track.class);

		if (trackTypeMap == null) {
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

			trackTypeMap = modelMapper.createTypeMap(TrackRequestDTO.class, Track.class);
            
			
			trackTypeMap.addMappings(mapper -> {
				mapper.using(genreConverter).map(TrackRequestDTO::getGenres, Track::setGenres);

				mapper.<String>map(src -> src.getAlbum().getTitle(), (Dest, v) -> Dest.getAlbum().setTitle(v));
				
				mapper.<String>map(src -> src.getAlbum().getBand(), (Dest, v) -> Dest.getAlbum().getBand().setName(v));
				

			});
		}

		return modelMapper.map(trackRequestDTO, Track.class);

	}
}
