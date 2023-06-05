package com.github.pablomathdev;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.infraestructure.BandRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BandRepositoryTests {

	static final String SELECT_BAND_BY_NAME = "select b from Band b where b.name = :name";

	@Mock
	TypedQuery<Band> typedQueryBand;

	@Mock
	EntityManager entityManager;

	@InjectMocks
	BandRepositoryImpl bandRepositoryImpl;

	@Test
	public void should_InvokeEntityManagerPersist_withCorrectArguments() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		bandRepositoryImpl.save(band);

		verify(entityManager).persist(eq(band));

	}

	@Test
	public void should_InvokeTypedQuery_withCorrectArguments() {

		Origin origin = originFactory("any_city", "any_country", 1999);
		Genre genre = genreFactory("any_genre");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		Band band = bandFactory("any_band", origin, set);

		List<Band> result = new ArrayList<>();
		result.add(band);

		when(entityManager.createQuery(SELECT_BAND_BY_NAME, Band.class)).thenReturn(typedQueryBand);

		bandRepositoryImpl.findByName(band.getName());

		verify(typedQueryBand).setParameter(eq("name"), eq(band.getName()));

	}
	
	

}
