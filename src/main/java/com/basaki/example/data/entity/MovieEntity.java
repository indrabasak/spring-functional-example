package com.basaki.example.data.entity;

import com.basaki.example.model.Genre;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by indra.basak on 6/11/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class MovieEntity implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private Genre genre;

    @Column(name = "star", nullable = false)
    private int star;

    @Column(name = "director_first_name", nullable = false)
    private String directorFirstName;

    @Column(name = "director_last_name", nullable = false)
    private String directorLastName;
}