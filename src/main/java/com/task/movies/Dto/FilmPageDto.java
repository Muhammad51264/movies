package com.task.movies.Dto;

import com.task.movies.model.Director;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilmPageDto {

    private long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "name is required")
    private Director director;

    public FilmPageDto(long id, String name, Director director) {
        this.id = id;
        this.name = name;
        this.director = director;
    }
}
