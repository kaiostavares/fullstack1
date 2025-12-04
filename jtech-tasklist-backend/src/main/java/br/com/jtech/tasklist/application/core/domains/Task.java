/*
*  @(#)Task.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.application.core.exceptions.DomainInvalidArgumentException;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@ToString
public class Task extends AbstractDomainEntity {
    private String name;
    private String description;
    private TaskStatus status;
    private OffsetDateTime createdAt;

    public Task() {
        super();
    }

    public Task(String name, String description, TaskStatus status) {
        setName(name);
        setDescription(description);
        setStatus(status);
        this.createdAt = OffsetDateTime.now();
    }

    public Task(String id, String name, String description, TaskStatus status) {
        super(id);
        setName(name);
        setDescription(description);
        setStatus(status);
    }

    public Task(String id, String name, String description, TaskStatus status, OffsetDateTime createdAt) {
        super(id);
        setName(name);
        setDescription(description);
        setStatus(status);
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainInvalidArgumentException("error.task.name.null_or_empty");
        }
        if (name.length() > 50) {
            throw new DomainInvalidArgumentException("error.task.name.too_long");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainInvalidArgumentException("error.task.description.null_or_empty");
        }
        if (description.length() > 500) {
            throw new DomainInvalidArgumentException("error.task.description.too_long");
        }
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new DomainInvalidArgumentException("error.task.status.null_or_empty");
        }
        this.status = status;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}