package net.lawyd.server;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Controller
@Path("api/todo")
public class TodoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        return "";
    }
}
