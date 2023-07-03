package com.github.pablomathdev.presentation.v1.DTOs.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AlbumRequestDTO {

	@NotBlank
	private String title;

	@Valid
	@NotNull
	private String band;

	@NotNull
	private LocalDate releaseDate; 
	
	@Valid
	@NotNull
	private List<String> genres;
}
