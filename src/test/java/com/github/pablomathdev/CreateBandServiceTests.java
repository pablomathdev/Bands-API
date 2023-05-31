package com.github.pablomathdev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.pablomathdev.application.CreateBandService;
import com.github.pablomathdev.domain.entities.Album;
import com.github.pablomathdev.domain.entities.Band;
import com.github.pablomathdev.domain.entities.Genre;
import com.github.pablomathdev.domain.entities.Member;
import com.github.pablomathdev.domain.entities.Origin;
import com.github.pablomathdev.domain.repositories.BandRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CreateBandServiceTests {
	
	
    @Captor ArgumentCaptor<Band> bandCaptor;
    
    @Mock
	BandRepository bandRepository;
    
	
    @InjectMocks
	CreateBandService createBandService;

	@Test
	public void should_call_save_method_repository_with_band() {

		Band band = new Band();
		band.setId(1);
		band.setName("Metallica");
		Origin origin = new Origin();
		origin.setCountry("United States");
		origin.setCity("San Francisco");
		band.setOrigin(origin);
		Album album = new Album();
		album.setId(1);
		album.setTitle("Master of Puppets");
		band.setAlbums(Arrays.asList(album));
		Member member = new Member();
		member.setName("James Hatfield");
		band.setMembers(Arrays.asList(member));
		Genre genre = new Genre();
		genre.setName("Trash Metal");
		Set<Genre> set = new HashSet<>();
		set.add(genre);
		band.setGenres(set);
		
		
		createBandService.execute(band);

		 Mockito.verify(bandRepository).save(bandCaptor.capture());
	       assertEquals(band, bandCaptor.getValue());
		
		
	}

}
