package com.basaki.example.model.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by indra.basak on 6/11/17.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie extends MovieRequest {
    private UUID id;
}
