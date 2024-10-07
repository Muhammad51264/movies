package com.task.movies.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class FilmDto {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "directorId is required")
    private long directorId;

    @NotBlank(message = "directorId is required")
    private List<Long> actorsIds;
}
