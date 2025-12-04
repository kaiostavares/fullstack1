package br.com.jtech.tasklist.application.dto.task;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import br.com.jtech.tasklist.application.dto.OutputDto;
import lombok.Getter;

@Getter
public class TaskOutput extends OutputDto<Task> {
    private String id;
    private String name;
    private String description;
    private TaskStatus status;

    public TaskOutput(Task entity) {
        super(entity);
    }

    @Override
    public OutputDto<Task> fromEntity(Task entity) {
        this.id = entity.getId();
        this.name =  entity.getName();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
        return this;
    }
}
