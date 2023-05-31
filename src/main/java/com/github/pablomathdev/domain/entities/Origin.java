package com.github.pablomathdev.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Origin {
 
	private String country;
	
	private String city;
	
	private Integer formationYear;
}
