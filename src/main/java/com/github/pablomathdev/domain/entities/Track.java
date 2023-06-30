package com.github.pablomathdev.domain.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_track")
public class Track {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;

	@ManyToOne
	@JoinColumn(name = "album_id")
	private Album album;
	
	
	@ManyToOne
	@JoinColumn(name = "single_id")
	private Single single;
	
	@JoinColumn(name = "release_date")
	private LocalDate releaseDate;
	
	
	@ManyToMany
	@JoinTable(name = "tb_track_genre",
	joinColumns = @JoinColumn(name="track_id"),
	inverseJoinColumns = @JoinColumn(name ="genre_id"))
	private List<Genre> genres;

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		return Objects.equals(title, other.title);
	}



	
}
