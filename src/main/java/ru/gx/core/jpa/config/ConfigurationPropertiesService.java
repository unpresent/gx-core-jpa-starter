package ru.gx.core.jpa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "service.db-saving")
@Getter
@Setter
public class ConfigurationPropertiesService {
    @NestedConfigurationProperty
    private JpaSaver jpaSaver;

    @NestedConfigurationProperty
    private Operator jpaJsonOperator;

    @NestedConfigurationProperty
    private Operator jpaBinaryOperator;

    @Getter
    @Setter
    public static class JpaSaver {
        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class Operator {
        public boolean enabled = true;
    }
}
