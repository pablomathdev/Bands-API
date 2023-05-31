package com.github.pablomathdev.domain.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_band")
public class Band {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Embedded
	private Origin origin;
	
	@OneToMany(mappedBy = "band")
	private List<Album> albums;
	
	
	@ManyToMany
	@JoinTable(name = "tb_band_genre",
	joinColumns = @JoinColumn(name= "band_id"),
	inverseJoinColumns = @JoinColumn(name= "genre_id"))
	private Set<Genre> genres = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "tb_band_member",
	joinColumns = @JoinColumn(name="band_id"),
	inverseJoinColumns = @JoinColumn(name="member_id"))
	private List<Member> members; 
	
	
	
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
		Band other = (Band) obj;
		return Objects.equals(name, other.name);
	}

}
