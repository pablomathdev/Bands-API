package com.github.pablomathdev.presentation.v1.DTOs.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandRequestDTO {

	@NotBlank
	private String name;

	@Valid
	@NotNull
	private OriginBandDTO origin;
	
	@Valid
	@NotNull
	private List<String> genres;
}
