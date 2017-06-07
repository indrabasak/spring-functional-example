package com.basaki.example.book.model;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

/**
 * {@code GenreTest} integration test class for {@link Genre}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@ActiveProfiles("test")
public class GenreTest {

    @Test
    public void testFromValue() {
        String name = Genre.ROMANCE.name().toLowerCase();

        Genre genre = Genre.fromValue(name);
        assertEquals(Genre.ROMANCE, genre);
    }
}
