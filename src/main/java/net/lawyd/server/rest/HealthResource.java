package net.lawyd.server.rest;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@Path("health")
public class HealthResource {

    @GET
    public String healthCheck() {
        return "Yay, I'm alive";
    }
}
