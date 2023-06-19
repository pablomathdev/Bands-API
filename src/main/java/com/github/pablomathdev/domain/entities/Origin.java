package com.github.pablomathdev.domain.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Origin {
 

	@NotBlank
	private String country;
	
    @NotBlank
	private String city;
	
    @NotNull
	private Integer formationYear;
}
