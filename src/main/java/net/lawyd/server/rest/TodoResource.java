package net.lawyd.server.rest;

import net.lawyd.server.rest.model.TodoJson;
import net.lawyd.server.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Path("todo")
public class TodoResource {

    @Autowired
    private TodoService todoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TodoJson> getAll() {
        return todoService.findAllTodos().stream().map(TodoJson::new).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TodoJson createTodo(TodoJson todoJson) {
        return new TodoJson(todoService.saveTodo(todoJson.convertToDbEntity()));
    }
}
