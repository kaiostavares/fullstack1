package br.com.jtech.tasklist.application.ports.input.common;

import br.com.jtech.tasklist.application.dto.CommandDto;
import br.com.jtech.tasklist.application.dto.OutputDto;

public interface UpdateEntityUseCase <C extends CommandDto<?>, O extends OutputDto<?>> {
    O execute (C command);
}
