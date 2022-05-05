package ru.gx.core.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.jpa.ActiveSessionsContainer;
import ru.gx.core.jpa.save.*;

@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
public class CommonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ActiveSessionsContainer activeSessionsContainer() {
        return new ActiveSessionsContainer();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    public JpaJsonDbSavingOperator jpaJsonDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveSessionsContainer activeSessionsContainer
    ) {
        return new JpaJsonDbSavingOperator(objectMapper, activeSessionsContainer);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    public JpaBinaryDbSavingOperator jdbcBinaryDbSavingOperator(
            @NotNull final ObjectMapper objectMapper,
            @NotNull final ActiveSessionsContainer activeSessionsContainer
    ) {
        return new JpaBinaryDbSavingOperator(objectMapper, activeSessionsContainer);
    }}
