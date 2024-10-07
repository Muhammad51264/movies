package com.task.movies.api.controllers;


import com.task.movies.Dto.DirectorDto;
import com.task.movies.model.Director;
import com.task.movies.model.User;
import com.task.movies.repository.DirectorRepository;
import com.task.movies.repository.UserRepository;
import com.task.movies.service.DirectorService;
import com.task.movies.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/director")
public class DirectorsController {

    private final DirectorService directorService;
    private final JwtUtil jwtUtil;
    private final DirectorRepository directorRepository;
    private final UserRepository userRepository;

    public DirectorsController(DirectorService directorService, JwtUtil jwtUtil, UserRepository userRepository, DirectorRepository directorRepository) {
        this.jwtUtil = jwtUtil;
        this.directorRepository = directorRepository;
        this.directorService = directorService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<List<Director>> getAllDirectors(HttpServletRequest request) {

        List<Director> allDirectors = directorService.getAllDirectors();

        return new ResponseEntity<>(allDirectors, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Director> createDirector(@RequestBody DirectorDto directorDto, HttpServletRequest request) {
        // Extract user email or ID from JWT token
        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);
        // Set the user to the actor
        // Save the actor
        Director savedDirector = directorRepository.save(new Director(directorDto.getName(), user));
        return new ResponseEntity<>(savedDirector, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Long id) {
        Director director = directorRepository.findById(id).orElse(null);
        if (director == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Director> editDirector(@PathVariable Long id, @RequestBody DirectorDto directorDto, HttpServletRequest request) {

        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);

        Director director = directorService.updateDirector(id, new Director(directorDto.getName(), user));
        if (director == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(director, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Director> deleteDirector(@PathVariable Long id) {

        Director director = directorRepository.findById(id).orElse(null);
        if (director == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        directorService.deleteDirector(id);

        return new ResponseEntity<>(director, HttpStatus.OK);
    }
}
