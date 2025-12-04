package br.com.jtech.tasklist.application.dto;

import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;

public interface CommandDto<T extends AbstractDomainEntity> extends BaseDto {
    T toEntity();
}
