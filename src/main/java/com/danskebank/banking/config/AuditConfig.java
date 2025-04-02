package com.danskebank.banking.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system");
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put("org.hibernate.envers.audit_table_suffix", "_AUD");
            hibernateProperties.put("org.hibernate.envers.revision_field_name", "REV");
            hibernateProperties.put("org.hibernate.envers.revision_type_field_name", "REVTYPE");
            hibernateProperties.put("org.hibernate.envers.track_entities_changed_in_revision", "false");
        };
    }
} 