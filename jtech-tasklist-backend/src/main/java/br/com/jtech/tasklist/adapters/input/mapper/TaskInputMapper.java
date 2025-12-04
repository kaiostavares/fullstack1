package br.com.jtech.tasklist.adapters.input.mapper;

import br.com.jtech.tasklist.adapters.input.dto.CreateTaskRequest;
import br.com.jtech.tasklist.adapters.input.dto.TaskResponse;
import br.com.jtech.tasklist.adapters.input.dto.UpdateTaskRequest;
import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskInputMapper {
    
    CreateTaskCommand toCreateCommand(CreateTaskRequest request);
    
    default UpdateTaskCommand toUpdateCommand(UpdateTaskRequest request, String id) {
        return new UpdateTaskCommand(id, request.name(), request.description(), request.status());
    }
    
    TaskResponse toResponse(TaskOutput output);
}

