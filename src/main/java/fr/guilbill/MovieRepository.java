package fr.guilbill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    
    private final MongoClient mongoClient;
    private final MongoCollection moviesCollection;
    
    public MovieRepository(String host) {
        mongoClient = new MongoClient(host);
        // Jongo still use the deprecated method getDB (https://github.com/bguerout/jongo/issues/254)
        moviesCollection = new Jongo(mongoClient.getDB("tp-mongo")).getCollection("movie");
    }
    
    public void save(Movie movie) {
        //TODO
        throw new NotImplementedException();
    }
    
    public Iterable<Movie> find() {
        //TODO
        throw new NotImplementedException();
    }
    
    public void update(Movie movie) {
        //TODO
        throw new NotImplementedException();
    }
    
    public Movie findByTitle(String title) {
        //TODO
        throw new NotImplementedException();
    }
    
    public void delete(Movie movie) {
        //TODO
        throw new NotImplementedException();
    }
    
    public Iterable<Movie> findFiveBestVotedMovies() {
        //TODO
        throw new NotImplementedException();
    }
    
    public void loadFile(String filePath) throws URISyntaxException, FileNotFoundException {
        final URI moviesFileURI = Resources.getResource(filePath).toURI();
        JsonReader moviesReader = new JsonReader(new FileReader(new File(moviesFileURI)));
    
        final Gson jsonMapper = new Gson();
        Type moviesType = new TypeToken<ArrayList<Movie>>() {
        }.getType();
        List<Movie> movies = jsonMapper.fromJson(moviesReader, moviesType);
        
        movies.forEach(movie -> this.moviesCollection.save((Movie) movie));
    }
    
    public void clearDataBase() {
        moviesCollection.drop();
    }
    
    public void close() {
        mongoClient.close();
    }
}
