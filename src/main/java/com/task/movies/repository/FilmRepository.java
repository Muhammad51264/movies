package com.task.movies.repository;

import com.task.movies.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FilmRepository extends JpaRepository<Film, Long> {
    // Get all films by director ID
    // Get all films by director ID with pagination
    Page<Film> findByDirectorId(Long directorId, Pageable pageable);

    // Get all films where a specific actor is involved with pagination
    @Query("SELECT f FROM Film f JOIN f.actors a WHERE a.id = :actorId")
    Page<Film> findByActorId(Long actorId, Pageable pageable);
}
