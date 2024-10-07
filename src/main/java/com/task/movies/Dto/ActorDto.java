package com.task.movies.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActorDto {
    @NotBlank(message = "name is required")
    private String name;

}
