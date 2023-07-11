package com.github.pablomathdev.presentation.v1.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OriginBandDTO {

	 @NotBlank
	 private String country;
	 
	 @NotBlank
	 private String city;
	 
	 @NotNull
	 private Integer formationYear;
	 
	 
}
