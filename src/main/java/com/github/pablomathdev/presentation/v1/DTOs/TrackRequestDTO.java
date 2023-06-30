package com.github.pablomathdev.presentation.v1.DTOs;

import java.util.List;

import com.github.pablomathdev.domain.entities.Single;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TrackRequestDTO {
	
    
	@NotBlank
	private String title;

	@Valid
	@NotNull
	private AlbumTrackDTO album;
	
	private Single single;

	@NotBlank
	private String releaseDate;
	
	@Valid
	@NotNull
	private List<String> genres;
	
}
