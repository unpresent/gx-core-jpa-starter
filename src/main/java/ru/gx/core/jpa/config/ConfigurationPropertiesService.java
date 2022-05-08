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
    private Operator operator;

    @Getter
    @Setter
    public static class JpaSaver {
        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class Operator {
        public OperatorType operatorType = OperatorType.Json;
    }

    @SuppressWarnings("unused")
    public enum OperatorType {
        Json,
        Binary
    }
}
