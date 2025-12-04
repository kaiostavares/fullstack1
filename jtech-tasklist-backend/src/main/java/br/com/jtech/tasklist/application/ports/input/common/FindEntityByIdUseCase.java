package br.com.jtech.tasklist.application.ports.input.common;

import br.com.jtech.tasklist.application.dto.OutputDto;

public interface FindEntityByIdUseCase<O extends OutputDto<?>> {
    O execute (String entityId);
}
