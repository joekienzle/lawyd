package net.lawyd.server.config;

import net.lawyd.server.rest.HealthResource;
import net.lawyd.server.rest.TodoResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TodoResource.class);
        register(HealthResource.class);
    }
}
