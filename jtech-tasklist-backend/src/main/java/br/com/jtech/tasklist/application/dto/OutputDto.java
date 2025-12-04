package br.com.jtech.tasklist.application.dto;


import br.com.jtech.tasklist.application.core.domains.AbstractDomainEntity;

public abstract class OutputDto<T extends AbstractDomainEntity> implements BaseDto {
    public abstract OutputDto<T> fromEntity(T entity);
    public OutputDto(T entity) {
        fromEntity(entity);
    }
}
