package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
@DirtiesContext
class MovieInfoRepositoryIntegrationTest {

    @Autowired MovieInfoRepository movieInfoRepository;


    @BeforeEach
    void setup(){
        new MovieInfo(null,"Batman Begins",2005, Arrays.asList("Christian Bale","Michael Cane"), LocalDate.parse(
                "2005-06-15"));
        new MovieInfo(null,"BatMan the movie",1966, Arrays.asList("Adam West","Cesar Romero"), LocalDate.parse(
                "1966-06-15"));
        new MovieInfo(null,"BatMan Returns",1992, Arrays.asList("Michael Keaton"," Jack Nicholson"), LocalDate.parse(
                "1992-06-15"));
        new MovieInfo(null,"The Dark knight",2008, Arrays.asList("Christian Bale","HeathLedger"), LocalDate.parse(
                "2018-07-15"));
        new MovieInfo(null,"Dark knight Rises",2012, Arrays.asList("Christian Bale","Tom Hardy"), LocalDate.parse(
                "2012-07-20"));
    }

    @Test
    void findAll(){
        Flux<MovieInfo> allMoviesFlux = movieInfoRepository.findAll();

        a
    }
}
