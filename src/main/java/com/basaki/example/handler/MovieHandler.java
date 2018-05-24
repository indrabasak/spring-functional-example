package com.basaki.example.handler;

import com.basaki.example.data.entity.MovieEntity;
import com.basaki.example.data.repository.MovieRepository;
import com.basaki.example.model.movie.Director;
import com.basaki.example.model.movie.Movie;
import com.basaki.example.model.movie.MovieRequest;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by indra.basak on 6/11/17.
 */
@Component
@Slf4j
public class MovieHandler {

    private static final String MOVIE_URL = "/movies";

    private static final String MOVIE_BY_ID_URL = MOVIE_URL + "/{id}";

    private final MovieRepository repo;

    private final Mapper mapper;

    @Autowired
    public MovieHandler(MovieRepository repo, Mapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public RouterFunction<ServerResponse> getRoutes() {
        return route(POST(MOVIE_URL).and(accept(APPLICATION_JSON)),
                this::create)
                .andRoute(GET(MOVIE_BY_ID_URL).and(accept(APPLICATION_JSON)),
                        this::read)
                .filter((request, next) -> {
                    log.info(
                            "Before handler invocation: " + request.path());
                    return next.handle(request);
                });
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Movie> result =
                request.bodyToMono(MovieRequest.class)
                        .map(this::mapRequestToEntity)
                        .map(repo::save)
                        .map(this::mapEntityToModel);

        return ServerResponse.status(HttpStatus.CREATED).contentType(
                APPLICATION_JSON).body(result, Movie.class);
    }

    public Mono<ServerResponse> read(ServerRequest request) {
        Mono.justOrEmpty(request.pathVariable("id"))
                .map(UUID::fromString)
                .map(repo::findById)
                .map(entity -> mapEntityToModel(entity.get()))
                .map(model -> ServerResponse.ok().contentType(
                        APPLICATION_JSON).body(Mono.just(model), Movie.class))
                .onErrorMap(excp -> ServerResponse.status(HttpStatus.NOT_FOUND).contentType(
                                            APPLICATION_JSON).body(excp.getMessage()));

    }

    private MovieEntity mapRequestToEntity(MovieRequest req) {
        MovieEntity entity = mapper.map(req, MovieEntity.class);
        entity.setId(UUID.randomUUID());
        entity.setDirectorFirstName(
                req.getDirector().getFirstName());
        entity.setDirectorLastName(req.getDirector().getLastName());

        return entity;
    }

    private Movie mapEntityToModel(MovieEntity entity) {
        Movie movie = mapper.map(entity, Movie.class);
        Director director = new Director();
        director.setFirstName(entity.getDirectorFirstName());
        director.setLastName(entity.getDirectorLastName());
        movie.setDirector(director);

        return movie;
    }
}
