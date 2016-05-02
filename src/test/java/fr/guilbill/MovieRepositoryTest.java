package fr.guilbill;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.Instant;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by gbilley on 29/04/2016.
 */
public class MovieRepositoryTest {
    
    private static MovieRepository movieRepository;
    
    @BeforeClass
    public static void initRepository() {
        movieRepository = new MovieRepository("127.0.0.1");
    }
    
    @AfterClass
    public static void closeRepository() {
        movieRepository.close();
    }
    
    @After
    public void tearDown() throws Exception {
        movieRepository.clearDataBase();
    }
    
    @Test
    public void itShouldFindAllMovies() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        final Iterable<Movie> movies = movieRepository.find();
        
        //THEN
        assertThat(movies).hasSize(40);
    }
    
    @Test
    public void itShouldAddAMovie() throws Exception {
        //GIVEN
        Movie movie = new Movie();
        movie.setTitle("La classe Américaine");
        movie.setReleaseDate(Date.from(Instant.parse("1993-12-01T00:00:00Z")));
        
        //WHEN
        movieRepository.save(movie);
        
        //THEN
        Iterable<Movie> movies = movieRepository.find();
        assertThat(movies)
            .extracting("title", "releaseDate")
            .contains(
                tuple(movie.getTitle(), movie.getReleaseDate())
            );
    }
    
    @Test
    public void itShouldFindMovieByTitle() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        Movie movie = movieRepository.findByTitle("Deadpool");
        
        //THEN
        assertThat(movie.getTitle()).isEqualTo("Deadpool");
    }
    
    @Test
    public void itShouldNotFindMovieWhenSearchingNonExistingMovie() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        Movie movie = movieRepository.findByTitle("NonExistingMovie");
        
        //THEN
        assertThat(movie).isNull();
    }
    
    @Test
    public void itShouldModifyAnExistingMovie() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        Movie movie = movieRepository.find().iterator().next();
        final String modifiedTitle = "Modified";
        final String modifiedOverview = "Modified overview";
        movie.setTitle(modifiedTitle);
        movie.setOverview(modifiedOverview);
        
        movieRepository.update(movie);
        
        //THEN
        Movie updatedMovie = movieRepository.findByTitle(modifiedTitle);
        assertThat(updatedMovie.getTitle()).isEqualTo(modifiedTitle);
        assertThat(updatedMovie.getOverview()).isEqualTo(modifiedOverview);
        
    }
    
    @Test
    public void itShoulRemoveAnExistingMovie() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        Movie movie = movieRepository.findByTitle("Deadpool");
        movieRepository.delete(movie);
        
        //THEN
        assertThat(movieRepository.findByTitle("Deadpool")).isNull();
        
    }
    
    @Test
    public void itShouldFindFiveBestVotedMovies() throws Exception {
        //GIVEN
        movieRepository.loadFile("movies-db.json");
        
        //WHEN
        Iterable<Movie> bestMovies = movieRepository.findFiveBestVotedMovies();
        
        //THEN
        assertThat(bestMovies)
            .extracting("title", "voteAverage")
            .containsExactly(
                tuple("Interstellar", 8.1895),
                tuple("Guardians of the Galaxy", 8.0098),
                tuple("The Imitation Game", 7.98),
                tuple("Big Hero 6", 7.83),
                tuple("Star War's", 7.79)
            );
    }
}
