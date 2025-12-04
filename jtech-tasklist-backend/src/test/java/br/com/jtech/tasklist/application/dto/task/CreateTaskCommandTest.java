package br.com.jtech.tasklist.application.dto.task;

/*
 *  @(#)CreateTaskCommandTest.java
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

@DisplayName("CreateTaskCommand Validation Tests")
class CreateTaskCommandTest {

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
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Task Name",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void shouldFailValidationWhenNameIsNull() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                null,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name is empty")
    void shouldFailValidationWhenNameIsEmpty() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "   ",
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when name exceeds max length")
    void shouldFailValidationWhenNameExceedsMaxLength() {
        // Given
        String longName = "a".repeat(51);
        CreateTaskCommand command = new CreateTaskCommand(
                longName,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.name.too_long");
    }

    @Test
    @DisplayName("Should fail validation when description is null")
    void shouldFailValidationWhenDescriptionIsNull() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                null,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when description is empty")
    void shouldFailValidationWhenDescriptionIsEmpty() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                "",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when description is blank")
    void shouldFailValidationWhenDescriptionIsBlank() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                "   ",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.mandatory");
    }

    @Test
    @DisplayName("Should fail validation when description exceeds max length")
    void shouldFailValidationWhenDescriptionExceedsMaxLength() {
        // Given
        String longDescription = "a".repeat(501);
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                longDescription,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.description.too_long");
    }

    @Test
    @DisplayName("Should fail validation when status is null")
    void shouldFailValidationWhenStatusIsNull() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                "Valid Description",
                null
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("error.task.status.mandatory");
    }

    @Test
    @DisplayName("Should fail validation with multiple errors")
    void shouldFailValidationWithMultipleErrors() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand(
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).hasSize(3);
    }

    @Test
    @DisplayName("Should validate successfully with name at max length")
    void shouldValidateSuccessfullyWithNameAtMaxLength() {
        // Given
        String maxName = "a".repeat(50);
        CreateTaskCommand command = new CreateTaskCommand(
                maxName,
                "Valid Description",
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should validate successfully with description at max length")
    void shouldValidateSuccessfullyWithDescriptionAtMaxLength() {
        // Given
        String maxDescription = "a".repeat(500);
        CreateTaskCommand command = new CreateTaskCommand(
                "Valid Name",
                maxDescription,
                TaskStatus.PENDING
        );

        // When
        Set<ConstraintViolation<CreateTaskCommand>> violations = validator.validate(command);

        // Then
        assertThat(violations).isEmpty();
    }
}
