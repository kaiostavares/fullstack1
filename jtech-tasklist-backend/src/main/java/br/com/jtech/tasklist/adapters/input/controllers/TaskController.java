/*
 *  @(#)TaskController.java
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
package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.dto.CreateTaskRequest;
import br.com.jtech.tasklist.adapters.input.dto.TaskResponse;
import br.com.jtech.tasklist.adapters.input.dto.UpdateTaskRequest;
import br.com.jtech.tasklist.adapters.input.mapper.TaskInputMapper;
import br.com.jtech.tasklist.adapters.input.facades.TaskFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operações CRUD de Task.
 * Expõe endpoints para criar, atualizar, consultar e deletar tarefas.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskFacade taskFacade;
    private final TaskInputMapper taskInputMapper;
    
    /**
     * Cria uma nova tarefa.
     *
     * @param request dados da tarefa a ser criada
     * @return resposta com os dados da tarefa criada
     */
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        var command = taskInputMapper.toCreateCommand(request);
        var output = taskFacade.createTask(command);
        var response = taskInputMapper.toResponse(output);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Atualiza uma tarefa existente.
     *
     * @param id ID da tarefa a ser atualizada
     * @param request dados da tarefa a ser atualizada
     * @return resposta com os dados da tarefa atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateTaskRequest request) {
        var command = taskInputMapper.toUpdateCommand(request, id);
        var output = taskFacade.updateTask(command);
        var response = taskInputMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Consulta uma tarefa pelo ID.
     *
     * @param id ID da tarefa a ser consultada
     * @return resposta com os dados da tarefa
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable String id) {
        var output = taskFacade.findTaskById(id);
        var response = taskInputMapper.toResponse(output);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as tarefas com paginação.
     *
     * @param pageable parâmetros de paginação (page, size, sort)
     * @return página contendo as tarefas encontradas
     */
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAll(Pageable pageable) {
        var output = taskFacade.findAllTasks(pageable);
        var response = output.map(taskInputMapper::toResponse);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Deleta uma tarefa.
     *
     * @param id ID da tarefa a ser deletada
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        taskFacade.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}