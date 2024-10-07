package com.task.movies.api.controllers;


import com.task.movies.Dto.FilmDto;
import com.task.movies.Dto.FilmPageDto;
import com.task.movies.model.Actor;
import com.task.movies.model.Director;
import com.task.movies.model.Film;
import com.task.movies.model.User;
import com.task.movies.repository.ActorRepository;
import com.task.movies.repository.DirectorRepository;
import com.task.movies.repository.FilmRepository;
import com.task.movies.repository.UserRepository;
import com.task.movies.service.FilmService;
import com.task.movies.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/film")
public class FilmsController {

    private final FilmService filmService;
    private final JwtUtil jwtUtil;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    public FilmsController(FilmService filmService, JwtUtil jwtUtil, UserRepository userRepository, FilmRepository filmRepository
            , ActorRepository actorRepository, DirectorRepository directorRepository) {
        this.filmService = filmService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
    }

    // Get films by director with pagination
    @GetMapping("/director/{directorId}")
    public ResponseEntity<List<FilmPageDto>> getFilmsByDirector(
            @PathVariable Long directorId,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit
            ) {

        List<Film> films = filmService.getFilmsByDirector(directorId, skip, limit);
        // Optionally handle include/exclude fields with DTO
        List<FilmPageDto> filmDtos = filmService.getFilmsWithSelectedFields(films);

        return new ResponseEntity<>(filmDtos, HttpStatus.OK);
    }

    // Get films by actor with pagination
    @GetMapping("/actor/{actorId}")
    public ResponseEntity<List<Film>> getFilmsByActor(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit) {

        List<Film> films = filmService.getFilmsByActor(actorId, skip, limit);
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms(HttpServletRequest request) {

        List<Film> allActors = filmService.getAllFilms();

        return new ResponseEntity<>(allActors, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Film> createFilm(@RequestBody FilmDto filmDto, HttpServletRequest request) {
        // Extract user email or ID from JWT token
        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);
        // Set the user to the actor
        // Save the actor
        Director director = directorRepository.findById(filmDto.getDirectorId())
                .orElseThrow(() -> new RuntimeException("Director not found"));

        List<Actor> actors = filmDto.getActorsIds().stream()
                .map(actorId -> actorRepository.findById(actorId)
                        .orElseThrow(() -> new RuntimeException("Actor not found: " + actorId)))
                .collect(Collectors.toList());

        Film savedFilm = filmRepository.save(new Film(filmDto.getName(), user, director, actors));
        return new ResponseEntity<>(savedFilm, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        Film film = filmRepository.findById(id).orElse(null);
        if (film == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> editFilm(@PathVariable Long id, @RequestBody FilmDto filmDto, HttpServletRequest request) {

        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);

        Director director = directorRepository.findById(filmDto.getDirectorId())
                .orElseThrow(() -> new RuntimeException("Director not found"));

        List<Actor> actors = filmDto.getActorsIds().stream()
                .map(actorId -> actorRepository.findById(actorId)
                        .orElseThrow(() -> new RuntimeException("Actor not found: " + actorId)))
                .collect(Collectors.toList());

        Film film = filmService.updateFilm(id, new Film(filmDto.getName(), user, director, actors));
        if (film == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Film> deleteFilm(@PathVariable Long id) {

        Film film = filmRepository.findById(id).orElse(null);
        if (film == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        filmService.deleteFilm(id);

        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}
