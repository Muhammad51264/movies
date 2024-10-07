package com.task.movies.service;

import com.task.movies.model.Director;

import java.util.List;

public interface DirectorService {

    List<Director> getAllDirectors();

    Director updateDirector(Long id, Director director);

    void deleteDirector(Long id);

}
