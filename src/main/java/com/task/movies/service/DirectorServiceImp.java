package com.task.movies.service;

import com.task.movies.model.Director;
import com.task.movies.repository.DirectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DirectorServiceImp implements DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorServiceImp(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }


    @Override
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }


    @Override
    public Director updateDirector(Long id, Director directorDetails) {
        Director director = directorRepository.findById(id).orElseThrow(() -> new RuntimeException("Director not found"));

        director.setName(directorDetails.getName());
        return directorRepository.save(director);
    }

    @Override
    public void deleteDirector(Long id) {
        directorRepository.deleteById(id);
    }
}
