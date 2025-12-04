package br.com.jtech.tasklist.adapters.input.dto;

import br.com.jtech.tasklist.application.core.domains.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponse(
    String id,
    String name,
    String description,
    TaskStatus status,
    OffsetDateTime createdAt
) {}
