package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.bandFactory;
import static com.github.pablomathdev.Factory.genreFactory;
import static com.github.pablomathdev.Factory.originFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.CreateBandService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Origin;



@SpringBootTest
@TestPropertySource("/application-test.properties")
public class CreateBandServiceIntegrationTest {

	@Autowired
	private CreateBandService createBandService;

	@Test
	public void should_CreateBandSuccessfully() {
		Origin origin = originFactory("Los Angeles", "United States", 1983);
		Genre genre = genreFactory("Trash Metal");
		Set<Genre> genres = new HashSet<>();
		genres.add(genre);

		Band band = bandFactory("Megadeth", origin, genres);

		Band bandSaved = createBandService.execute(band);

		assertNotNull(bandSaved.getId());
		assertEquals(bandSaved.getName(),band.getName());

	}

}
