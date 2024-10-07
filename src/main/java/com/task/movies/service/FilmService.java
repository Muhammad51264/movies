package com.task.movies.service;

import com.task.movies.Dto.FilmPageDto;
import com.task.movies.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getAllFilms();

    Film updateFilm(Long id, Film film);

    void deleteFilm(Long id);

    List<Film> getFilmsByDirector(Long directorId, int skip, int limit);

    List<Film> getFilmsByActor(Long actorId, int skip, int limit);

    List<FilmPageDto> getFilmsWithSelectedFields(List<Film> films);
}
