package com.github.pablomathdev.domain.entities;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_single")
public class Single extends Release{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "band_id")
	private Band band;

	@ManyToMany
	@JoinTable(name = "tb_single_genre",
	joinColumns = @JoinColumn(name = "single_id"), 
	inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Single other = (Single) obj;
		return Objects.equals(id, other.id);
	}


	
}
