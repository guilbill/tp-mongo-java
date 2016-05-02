package fr.guilbill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mongodb.MongoClient;

/**
 * Created by gbilley on 29/04/2016.
 */
public class MovieRepository {
    
    private final Gson jsonMapper = new Gson();
    private final MongoClient mongoClient;
    private final MongoCollection moviesCollection;
    
    public MovieRepository(String host) {
        mongoClient = new MongoClient(host);
        // Jongo still use the deprecated method getDB (https://github.com/bguerout/jongo/issues/254)
        moviesCollection = new Jongo(mongoClient.getDB("tp-mongo")).getCollection("movie");
    }
    
    public void save(Movie movie) {
        //TODO
        moviesCollection.save(movie);
    }
    
    public Iterable<Movie> find() {
        //TODO
        Iterable<Movie> movies = this.moviesCollection.find().as(Movie.class);
        return movies;
    }
    
    public void update(Movie movie) {
        //TODO
        moviesCollection.update(movie.getId()).with(movie);
    }
    
    public Movie findByTitle(String title) {
        //TODO
        return moviesCollection.findOne("{title:'" + title + "'}").as(Movie.class);
    }
    
    public void loadFile(String filePath) throws URISyntaxException, FileNotFoundException {
        final URI moviesFileURI = Resources.getResource(filePath).toURI();
        JsonReader moviesReader = new JsonReader(new FileReader(new File(moviesFileURI)));
        
        Type moviesType = new TypeToken<ArrayList<Movie>>() {
        }.getType();
        List<Movie> movies = jsonMapper.fromJson(moviesReader, moviesType);
        
        movies.forEach(movie -> this.moviesCollection.save((Movie) movie));
    }
    
    public void clearDataBase() {
        moviesCollection.drop();
    }
    
    public void delete(Movie movie) {
        moviesCollection.remove(movie.getId());
    }
    
    public Iterable<Movie> findFiveBestVotedMovies() {
        return moviesCollection.find().sort("{voteAverage:-1}").limit(5).as(Movie.class);
    }
    
    public void close() {
        mongoClient.close();
    }
}
