package com.task.movies.service;

import com.task.movies.Dto.FilmPageDto;
import com.task.movies.model.Film;
import com.task.movies.repository.FilmRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FilmServiceImp implements FilmService {

    private final FilmRepository filmRepository;

    public FilmServiceImp(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film updateFilm(Long id, Film filmDetails) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Actor not found"));

        film.setName(filmDetails.getName());
        film.setDirector(filmDetails.getDirector());
        film.setActors(filmDetails.getActors());
        return filmRepository.save(film);
    }

    @Override
    public void deleteFilm(Long id) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Film not found"));

        film.getActors().clear();

        // Save the film to update the relationship in the database
        filmRepository.save(film);

        // Now delete the film
        filmRepository.deleteById(id);
    }


    public List<FilmPageDto> getFilmsWithSelectedFields(List<Film> films) {
        return films.stream()
                .map(film -> new FilmPageDto(film.getId(), film.getName(), film.getDirector()))
                .collect(Collectors.toList());
    }


    // Get films by director ID with pagination
    @Override
    public List<Film> getFilmsByDirector(Long directorId, int skip, int limit) {
        Pageable pageable = PageRequest.of(skip, limit);
        return filmRepository.findByDirectorId(directorId, pageable).getContent();
    }

    // Get films by actor ID with pagination
    @Override
    public List<Film> getFilmsByActor(Long actorId, int skip, int limit) {
        Pageable pageable = PageRequest.of(skip, limit);
        return filmRepository.findByActorId(actorId, pageable).getContent();
    }
}
