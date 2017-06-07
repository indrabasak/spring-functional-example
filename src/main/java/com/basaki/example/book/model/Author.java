package com.basaki.example.book.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code Book} represents an author.
 * <p/>
 *
 * @author Indra Basak
 * @since 3/13/17
 */
@Data
@NoArgsConstructor
public class Author {
    private String firstName;

    private String lastName;
}
