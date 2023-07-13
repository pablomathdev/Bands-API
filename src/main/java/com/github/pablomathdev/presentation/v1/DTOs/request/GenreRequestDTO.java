package com.github.pablomathdev.presentation.v1.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GenreRequestDTO {

	@NotBlank
	private String name;
}
