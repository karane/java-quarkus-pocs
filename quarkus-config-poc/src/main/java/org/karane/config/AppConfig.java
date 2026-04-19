package org.karane.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    String greeting();

    @WithDefault("10")
    int maxItems();

    @WithDefault("false")
    boolean featureFlag();

    Database database();

    interface Database {
        @WithDefault("localhost")
        String host();

        @WithDefault("5432")
        int port();

        @WithDefault("mydb")
        String name();
    }
}
