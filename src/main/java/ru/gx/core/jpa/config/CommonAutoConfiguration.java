package ru.gx.core.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.data.save.DbSavingConfiguration;
import ru.gx.core.jpa.save.JpaBinaryDbSavingOperator;
import ru.gx.core.jpa.save.JpaDbSaver;
import ru.gx.core.jpa.save.JpaJsonDbSavingOperator;
import ru.gx.core.jpa.sqlwrapping.JpaThreadConnectionsWrapper;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
public class CommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JpaThreadConnectionsWrapper jpaThreadConnectionsWrapper(
            @NotNull final SessionFactory sessionFactory
    ) {
        return new JpaThreadConnectionsWrapper(sessionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jpa-json-operator.enabled",
            havingValue = "true"
    )
    public JpaJsonDbSavingOperator jpaJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JpaThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        return new JpaJsonDbSavingOperator(objectMapper, threadConnectionsWrapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jpa-binary-operator.enabled",
            havingValue = "true"
    )
    public JpaBinaryDbSavingOperator jpaBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final JpaThreadConnectionsWrapper threadConnectionsWrapper
    ) {
        return new JpaBinaryDbSavingOperator(objectMapper, threadConnectionsWrapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            value = "service.db-saving.jpa-saver.enabled",
            havingValue = "true"
    )
    public JpaDbSaver jdbcDbSaver(
            @NotNull final List<DbSavingConfiguration> configurations
    ) {
        return new JpaDbSaver(configurations);
    }
}
