package br.com.jtech.tasklist.adapters.input.dto;

import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @NotBlank(message = "error.task.name.mandatory")
    @Size(max = 50, message = "error.task.name.too_long")
    String name,

    @NotBlank(message = "error.task.description.mandatory")
    @Size(max = 500, message = "error.task.description.too_long")
    String description,

    @NotNull(message = "error.task.status.mandatory")
    TaskStatus status
) {}
