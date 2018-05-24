package com.basaki.example.handler;

import com.basaki.example.model.Genre;
import com.basaki.example.model.book.Book;
import com.basaki.example.model.book.BookRequest;
import com.basaki.example.service.BookService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by indra.basak on 6/8/17.
 */
@Component
public class BookHandler {

    private static final String BOOK_URL = "/booxs";

    private static final String BOOK_BY_ID_URL = BOOK_URL + "/{id}";

    private BookService service;

    @Autowired
    public BookHandler(BookService service) {
        this.service = service;
    }

    public RouterFunction<ServerResponse> getRoutes() {
        return route(POST(BOOK_URL).and(contentType(
                APPLICATION_JSON).and(accept(APPLICATION_JSON))),
                this::create)
                .andRoute(GET(BOOK_BY_ID_URL), this::read)
                .andRoute(GET(BOOK_URL), this::readAll);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Book> book = request.bodyToMono(BookRequest.class)
                .map(service::create);

        return ServerResponse.status(HttpStatus.CREATED).contentType(
                APPLICATION_JSON).body(book, Book.class);
    }

    public Mono<ServerResponse> read(ServerRequest request) {
        try {
            Book book =
                    service.read(UUID.fromString(request.pathVariable("id")));
            return ServerResponse.ok().contentType(
                    APPLICATION_JSON).body(fromObject(book));
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.NOT_FOUND).contentType(
                    APPLICATION_JSON).body(fromObject(e.getMessage()));
        }
    }

    public Mono<ServerResponse> readAll(ServerRequest request) {
        String title = getQueryParam(request, "title");
        String genValue = getQueryParam(request, "genre");
        Genre genre = genValue != null ? Genre.fromValue(genValue) : null;
        String publisher = getQueryParam(request, "publisher");
        String authorFirstName = getQueryParam(request, "firstName");
        String authorLastName = getQueryParam(request, "lastName");

        Flux<Book> bookFlux = Flux.fromIterable(
                service.readAll(title, genre, publisher, authorFirstName,
                        authorLastName));

        return ServerResponse.ok().contentType(APPLICATION_JSON).body(bookFlux,
                Book.class);
    }

    private String getQueryParam(ServerRequest request, String name) {
        Optional<String> value = request.queryParam(name);
        if (value.isPresent()) {
            return value.get();
        }

        return null;
    }

}
