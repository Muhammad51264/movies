package com.task.movies.service;

import com.task.movies.model.Actor;

import java.util.List;

public interface ActorService {

    List<Actor> getAllActors();

    Actor updateActor(Long id, Actor actor);

    void deleteActor(Long id);

}
