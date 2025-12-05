package br.com.jtech.tasklist.application.ports.input.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllEntitiesUseCase<O> {
    Page<O> execute(Pageable pageable);
}
