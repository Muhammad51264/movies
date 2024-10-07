package com.task.movies.service;

import com.task.movies.model.Actor;
import com.task.movies.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActorServiceImp implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImp(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }


    @Override
    public Actor updateActor(Long id, Actor actorDetails) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new RuntimeException("Actor not found"));

        actor.setName(actorDetails.getName());
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }
}
