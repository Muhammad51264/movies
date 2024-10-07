package com.task.movies.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "films")
@Data
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "director_id",nullable = false)
    private Director director;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "films_actors", joinColumns = @JoinColumn(name = "film_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id",referencedColumnName = "id")
    )
    private List<Actor> actors;

    public Film() {
    }


    public Film(String name, User user, Director director, List<Actor> actors) {
        this.name = name;
        this.user = user;
        this.director = director;
        this.actors = actors;
    }
}
