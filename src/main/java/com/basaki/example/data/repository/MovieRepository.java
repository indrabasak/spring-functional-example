package com.basaki.example.data.repository;

import com.basaki.example.data.entity.MovieEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by indra.basak on 6/11/17.
 */
@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, UUID> {
}
