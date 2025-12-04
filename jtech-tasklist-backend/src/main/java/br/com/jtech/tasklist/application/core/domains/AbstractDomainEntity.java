package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.application.core.exceptions.DomainInvalidArgumentException;

import java.util.Objects;

public abstract class AbstractDomainEntity {
    private String id;

    public AbstractDomainEntity() {
        this.id = null;
    }

    public AbstractDomainEntity(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new DomainInvalidArgumentException("error.domain.id.null_or_empty");
        }
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractDomainEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
