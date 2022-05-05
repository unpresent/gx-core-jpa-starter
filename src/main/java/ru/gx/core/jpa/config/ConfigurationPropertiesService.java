package ru.gx.core.jpa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "service")
@Getter
@Setter
public class ConfigurationPropertiesService {

    @NestedConfigurationProperty
    private ActiveSessionsContainer activeSessionsContainer;

    @Getter
    @Setter
    public static class ActiveSessionsContainer {
        private boolean enabled;
    }
}
