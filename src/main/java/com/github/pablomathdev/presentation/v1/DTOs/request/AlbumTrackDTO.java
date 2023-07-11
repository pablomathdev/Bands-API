package com.github.pablomathdev.presentation.v1.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlbumTrackDTO {

	@NotBlank
	private String title;
	
	@NotBlank
	private String band;
}
