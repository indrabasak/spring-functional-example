package com.basaki.example.model.movie;

import com.basaki.example.model.Genre;
import lombok.Data;

/**
 * Created by indra.basak on 6/11/17.
 */
@Data
public class MovieRequest {
    private String title;
    private Genre genre;
    private int star;
    private Director director;
}
