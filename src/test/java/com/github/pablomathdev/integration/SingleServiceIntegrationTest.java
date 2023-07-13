package com.github.pablomathdev.integration;

import static com.github.pablomathdev.Factory.singleFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.pablomathdev.application.services.SingleService;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Single;
import com.github.pablomathdev.domain.exceptions.alreadyExistsException.SingleAlreadyExistsException;
import com.github.pablomathdev.domain.exceptions.notFoundExceptions.BandNotFoundException;
import com.github.pablomathdev.utils.ExecuteSQL;


@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SingleServiceIntegrationTest {

	@Autowired
	private ExecuteSQL executeSQL;

	@Autowired
	private SingleService singleService;

	@BeforeEach
	public void clearDatabaseTest() {
		executeSQL.run("clear_database_test.sql");
	}

	@BeforeEach
	public void prepareData() {
		executeSQL.run("data_test.sql");
	}

	@Test
	public void should_CreateSingleSuccessfully() {
		
		Band band = new Band();
		band.setName("Metallica");
		Genre genre = new Genre();
		genre.setName("Trash Metal");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Single single = singleFactory("Master of Puppets",band,genres,LocalDate.parse("1986-07-02"),null);

		Single singleSaved = singleService.create(single);
		
		assertNotNull(singleSaved.getId());
		assertEquals(single.getTitle(), singleSaved.getTitle());
		
		

	}

	@Test
	public void should_ThrowSingleAlreadyExistsException_WhenSingleAlreadyExists() {
		
		Band band = new Band();
		band.setName("Metallica");
		Genre genre = new Genre();
		genre.setName("Trash Metal");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Single single = singleFactory("Whiplash",band,genres,LocalDate.parse("1983-11-08"),null);
		
		
		assertThrows(SingleAlreadyExistsException.class, () -> singleService.create(single));

	}

	@Test
	public void should_ThrowBandNotFoundException_WhenBandOfSingleNotExists() {
		Band band = new Band();
		band.setName("Iron Maiden");
		Genre genre = new Genre();
		genre.setName("Heavy Metal");
		List<Genre> genres = new ArrayList<>();
		genres.add(genre);
		
		Single single = singleFactory("Run to the Hills",band,genres,LocalDate.parse("1982-12-12"),null);

		assertThrows(BandNotFoundException.class, () -> singleService.create(single));

	}

	@Test
	public void should_ReturnResultListOfSingles_WhenSinglesExists() {

		List<Single> result = singleService.findAll();

		assertEquals(1, result.size());

	}

	@Test
	public void should_ReturnResultListEmptyOfSingles_WhenSinglesNotExists() {

		clearDatabaseTest();

		List<Single> result = singleService.findAll();

		assertTrue(result.isEmpty());

	}
	
	@Test
	public void should_DeleteSingle_WhenSingleExists() {
		
		
		singleService.delete("Whiplash","Metallica");
		
		List<Single> singles = singleService.findAll();
		
		boolean singleIsRemoved = singles.stream().noneMatch(single -> single.getTitle()
				.equals("Whiplash") && single.getBand().getName()
				.equals("Metallica"));
		
		assertTrue(singleIsRemoved);
	}
}