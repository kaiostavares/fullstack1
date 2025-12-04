package br.com.jtech.tasklist.config.infra.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Configuração para fornecer OffsetDateTime para auditoria JPA.
 */
@Configuration
public class AuditingConfiguration {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
