package net.lawyd.server.config;

import net.lawyd.server.rest.HealthResource;
import net.lawyd.server.rest.TodoResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringBootConfiguration;

import javax.ws.rs.ApplicationPath;

@SpringBootConfiguration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TodoResource.class);
        register(HealthResource.class);
    }
}
