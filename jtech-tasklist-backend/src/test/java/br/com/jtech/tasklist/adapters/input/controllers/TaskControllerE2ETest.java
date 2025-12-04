package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.BaseE2ETest;
import br.com.jtech.tasklist.adapters.input.dto.CreateTaskRequest;
import br.com.jtech.tasklist.adapters.input.dto.TaskResponse;
import br.com.jtech.tasklist.adapters.input.dto.UpdateTaskRequest;
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TaskController End-to-End Tests with TestContainers")
class TaskControllerE2ETest extends BaseE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create task successfully and return 201 Created")
    void shouldCreateTaskSuccessfully() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest(
                "Test Task",
                "This is a test task",
                TaskStatus.PENDING
        );

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        TaskResponse response = objectMapper.readValue(responseBody, TaskResponse.class);
        assertThat(response.id()).isNotNull();
    }

    @Test
    @DisplayName("Should find task by id successfully")
    void shouldFindTaskByIdSuccessfully() throws Exception {
        // Create task first
        CreateTaskRequest createRequest = new CreateTaskRequest("Find Test Task", "Find description", TaskStatus.PENDING);
        MvcResult createResult = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String taskId = objectMapper.readValue(createResult.getResponse().getContentAsString(), TaskResponse.class).id();

        // Then find it
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Find Test Task"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("Should update task successfully and return 200 OK")
    void shouldUpdateTaskSuccessfully() throws Exception {
        // Create task first
        CreateTaskRequest createRequest = new CreateTaskRequest("Update Test Task", "Original description", TaskStatus.PENDING);
        MvcResult createResult = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String taskId = objectMapper.readValue(createResult.getResponse().getContentAsString(), TaskResponse.class).id();

        // Given
        UpdateTaskRequest request = new UpdateTaskRequest(
                "Updated Task",
                "Updated description",
                TaskStatus.IN_PROGRESS
        );

        // When & Then
        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("Should delete task successfully and return 204 No Content")
    void shouldDeleteTaskSuccessfully() throws Exception {
        // Create task first
        CreateTaskRequest createRequest = new CreateTaskRequest("Delete Test Task", "Will be deleted", TaskStatus.PENDING);
        MvcResult createResult = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String taskId = objectMapper.readValue(createResult.getResponse().getContentAsString(), TaskResponse.class).id();

        // When & Then
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when trying to find non-existent task")
    void shouldReturnNotFoundForNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", "00000000-0000-0000-0000-000000000000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when creating task with invalid data")
    void shouldReturnBadRequestOnInvalidCreateData() throws Exception {
        // Given - Missing required fields
        String invalidRequest = "{}";

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when name is too long")
    void shouldReturnBadRequestWhenNameTooLong() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest(
                "a".repeat(51),
                "This is a test task",
                TaskStatus.PENDING
        );

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when description is too long")
    void shouldReturnBadRequestWhenDescriptionTooLong() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest(
                "Test Task",
                "a".repeat(501),
                TaskStatus.PENDING
        );

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when status is null")
    void shouldReturnBadRequestWhenStatusIsNull() throws Exception {
        // Given
        String invalidRequest = """
                {
                    "name": "Test Task",
                    "description": "Test description",
                    "status": null
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create multiple tasks successfully")
    void shouldCreateMultipleTasksSuccessfully() throws Exception {
        // Given
        CreateTaskRequest request1 = new CreateTaskRequest("Multi Task 1", "Description 1", TaskStatus.PENDING);
        CreateTaskRequest request2 = new CreateTaskRequest("Multi Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        CreateTaskRequest request3 = new CreateTaskRequest("Multi Task 3", "Description 3", TaskStatus.COMPLETED);

        // When - Create first task
        MvcResult result1 = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andReturn();

        String taskId1 = objectMapper.readValue(result1.getResponse().getContentAsString(), TaskResponse.class).id();

        // When - Create second task
        MvcResult result2 = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andReturn();

        String taskId2 = objectMapper.readValue(result2.getResponse().getContentAsString(), TaskResponse.class).id();

        // When - Create third task
        MvcResult result3 = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isCreated())
                .andReturn();

        String taskId3 = objectMapper.readValue(result3.getResponse().getContentAsString(), TaskResponse.class).id();

        // Then - Verify all tasks exist
        assertThat(taskId1).isNotNull();
        assertThat(taskId2).isNotNull();
        assertThat(taskId3).isNotNull();
        assertThat(taskId1).isNotEqualTo(taskId2);
        assertThat(taskId2).isNotEqualTo(taskId3);

        // Then - Verify can retrieve all tasks
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Multi Task 1"));

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(get("/api/v1/tasks/{id}", taskId3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Should prevent creating task with duplicate name")
    void shouldPreventDuplicateTaskName() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest(
                "Unique Duplicate Test Name",
                "First task with this name",
                TaskStatus.PENDING
        );

        // When - Create first task
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // When & Then - Try to create second task with same name (should fail)
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update task status from PENDING to IN_PROGRESS to COMPLETED")
    void shouldUpdateTaskStatusThroughLifecycle() throws Exception {
        // Given - Create task
        CreateTaskRequest createRequest = new CreateTaskRequest(
                "Lifecycle Task Test",
                "Testing task status lifecycle",
                TaskStatus.PENDING
        );

        MvcResult createResult = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String taskId = objectMapper.readValue(createResult.getResponse().getContentAsString(), TaskResponse.class).id();

        // When - Update to IN_PROGRESS
        UpdateTaskRequest updateToInProgress = new UpdateTaskRequest(
                "Lifecycle Task Test",
                "Testing task status lifecycle",
                TaskStatus.IN_PROGRESS
        );

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateToInProgress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        // When - Update to COMPLETED
        UpdateTaskRequest updateToCompleted = new UpdateTaskRequest(
                "Lifecycle Task Test",
                "Testing task status lifecycle",
                TaskStatus.COMPLETED
        );

        mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateToCompleted)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        // Then - Verify final state
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
