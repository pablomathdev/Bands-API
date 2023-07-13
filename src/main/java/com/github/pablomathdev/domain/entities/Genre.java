package com.github.pablomathdev.domain.entities;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_genre")
public class Genre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	

	private String name;
	
	
	@Setter(value = AccessLevel.NONE)
	@ManyToMany(mappedBy = "genres")
	private Set<Band> bands;
	
	
	@Setter(value = AccessLevel.NONE)
	@ManyToMany(mappedBy = "genres")
	private List<Track> tracks;


	@Override
	public int hashCode() {
		return Objects.hash(name);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		return Objects.equals(name, other.name);
	}



    

	
}
