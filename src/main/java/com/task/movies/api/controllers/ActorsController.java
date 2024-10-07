package com.task.movies.api.controllers;


import com.task.movies.Dto.ActorDto;
import com.task.movies.model.Actor;
import com.task.movies.model.User;
import com.task.movies.repository.ActorRepository;
import com.task.movies.repository.UserRepository;
import com.task.movies.service.ActorService;
import com.task.movies.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/actor")
public class ActorsController {

    private final ActorService actorService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ActorRepository actorRepository;

    public ActorsController(ActorService actorService, JwtUtil jwtUtil, UserRepository userRepository, ActorRepository actorRepository) {
        this.actorService = actorService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.actorRepository = actorRepository;
    }


    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors(HttpServletRequest request) {

        List<Actor> allActors = actorService.getAllActors();

        return new ResponseEntity<>(allActors, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Actor> createActor(@RequestBody ActorDto actorDto, HttpServletRequest request) {
        // Extract user email or ID from JWT token
        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);
        // Set the user to the actor
        // Save the actor
        Actor savedActor = actorRepository.save(new Actor(actorDto.getName(), user));
        return new ResponseEntity<>(savedActor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorRepository.findById(id).orElse(null);
        if (actor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> editActor(@PathVariable Long id, @RequestBody ActorDto actorDto, HttpServletRequest request) {

        String userEmail = jwtUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        // Retrieve the user from the repository using the email or ID
        User user = userRepository.findByEmail(userEmail);

        Actor actor = actorService.updateActor(id, new Actor(actorDto.getName(), user));
        if (actor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Actor> deleteActor(@PathVariable Long id) {

        Actor actor= actorRepository.findById(id).orElse(null);
        if (actor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        actorService.deleteActor(id);

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }
}
