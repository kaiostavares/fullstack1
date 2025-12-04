package br.com.jtech.tasklist.application.dto.task;

/*
 *  @(#)UpdateTaskCommandTest.java
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
import br.com.jtech.tasklist.application.core.domains.TaskStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UpdateTaskCommand Validation Tests")
class UpdateTaskCommandTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should validate successfully with valid data")
    void shouldValidateSuccessfullyWithValidData() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Task Name",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when id is null")
    void shouldFailValidationWhenIdIsNull() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                null,
                "Valid Name",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.id.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when id is empty")
    void shouldFailValidationWhenIdIsEmpty() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "",
                "Valid Name",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.id.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when id is blank")
    void shouldFailValidationWhenIdIsBlank() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "   ",
                "Valid Name",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.id.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void shouldFailValidationWhenNameIsNull() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                null,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name is empty")
    void shouldFailValidationWhenNameIsEmpty() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name exceeds max length")
    void shouldFailValidationWhenNameExceedsMaxLength() {
        // Given
        String longName = "a".repeat(51);
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                longName,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.too_long");
    }

    @Test
    @DisplayName("Should fail validation when description is null")
    void shouldFailValidationWhenDescriptionIsNull() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Name",
                null,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when description is empty")
    void shouldFailValidationWhenDescriptionIsEmpty() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Name",
                "",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when description exceeds max length")
    void shouldFailValidationWhenDescriptionExceedsMaxLength() {
        // Given
        String longDescription = "a".repeat(501);
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Name",
                longDescription,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.too_long");
    }

    @Test
    @DisplayName("Should fail validation when status is null")
    void shouldFailValidationWhenStatusIsNull() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Name",
                "Valid Description",
                null
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.status.mandatory");
    }

    @Test
    @DisplayName("Should fail validation with multiple errors")
    void shouldFailValidationWithMultipleErrors() {
        // Given
        UpdateTaskCommand command = new UpdateTaskCommand(
                null,
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(4);
    }

    @Test
    @DisplayName("Should validate successfully with name at max length")
    void shouldValidateSuccessfullyWithNameAtMaxLength() {
        // Given
        String maxName = "a".repeat(50);
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                maxName,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should validate successfully with description at max length")
    void shouldValidateSuccessfullyWithDescriptionAtMaxLength() {
        // Given
        String maxDescription = "a".repeat(500);
        UpdateTaskCommand command = new UpdateTaskCommand(
                "550e8400-e29b-41d4-a716-446655440000",
                "Valid Name",
                maxDescription,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<UpdateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }
}
