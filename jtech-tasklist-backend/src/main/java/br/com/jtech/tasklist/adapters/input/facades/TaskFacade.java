package br.com.jtech.tasklist.adapters.input.facades;

import br.com.jtech.tasklist.application.dto.task.CreateTaskCommand;
import br.com.jtech.tasklist.application.dto.task.TaskOutput;
import br.com.jtech.tasklist.application.dto.task.UpdateTaskCommand;
import br.com.jtech.tasklist.application.ports.input.common.CreateEntityUseCase;
import br.com.jtech.tasklist.application.ports.input.common.DeleteEntityUseCase;
import br.com.jtech.tasklist.application.ports.input.common.FindAllEntitiesUseCase;
import br.com.jtech.tasklist.application.ports.input.common.FindEntityByIdUseCase;
import br.com.jtech.tasklist.application.ports.input.common.UpdateEntityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Facade que centraliza e simplifica o acesso aos casos de uso de Task.
 * Fornece uma interface unificada para operações CRUD de tarefas.
 */
@Service
@RequiredArgsConstructor
public class TaskFacade {

    private final CreateEntityUseCase<CreateTaskCommand, TaskOutput> createTaskUseCase;
    private final UpdateEntityUseCase<UpdateTaskCommand, TaskOutput> updateTaskUseCase;
    private final FindEntityByIdUseCase<TaskOutput> findTaskByIdUseCase;
    private final FindAllEntitiesUseCase<TaskOutput> findAllTasksUseCase;
    private final DeleteEntityUseCase deleteTaskUseCase;

    /**
     * Cria uma tarefa.
     *
     * @param command comando com os dados da tarefa a ser criada
     * @return TaskOutput com os dados da tarefa criada
     */
    public TaskOutput createTask(CreateTaskCommand command) {
        return createTaskUseCase.execute(command);
    }

    /**
     * Atualiza uma tarefa existente.
     *
     * @param command comando com os dados da tarefa a ser atualizada
     * @return TaskOutput com os dados da tarefa atualizada
     */
    public TaskOutput updateTask(UpdateTaskCommand command) {
        return updateTaskUseCase.execute(command);
    }

    /**
     * Busca uma tarefa por ID.
     *
     * @param taskId ID da tarefa a ser buscada
     * @return TaskOutput com os dados da tarefa encontrada
     */
    public TaskOutput findTaskById(String taskId) {
        return findTaskByIdUseCase.execute(taskId);
    }

    /**
     * Busca todas as tarefas com paginação.
     *
     * @param pageable parâmetros de paginação
     * @return Page contendo as tarefas encontradas
     */
    public Page<TaskOutput> findAllTasks(Pageable pageable) {
        return findAllTasksUseCase.execute(pageable);
    }

    /**
     * Deleta (soft delete) uma tarefa por ID.
     *
     * @param taskId ID da tarefa a ser deletada
     */
    public void deleteTask(String taskId) {
        deleteTaskUseCase.execute(taskId);
    }
}