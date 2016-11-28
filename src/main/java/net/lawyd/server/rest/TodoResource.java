package net.lawyd.server.rest;

import net.lawyd.server.persistence.Todo;
import net.lawyd.server.rest.model.TodoJson;
import net.lawyd.server.service.TodoService;
import net.lawyd.server.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Path("todo")
public class TodoResource {

    @Autowired
    private TodoService todoService;

    @GET
    @Path("{todoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodo(@PathParam(value = "todoId") String todoId) {
        Todo todoById;
        try {
            todoById = todoService.findTodoById(todoId);
        } catch (IllegalStateException e) {
            return buildPreconditionFailedResponse(e.getMessage());
        } catch (NotFoundException e) {
            return buildNotFoundResponse(e.getMessage());
        }
        return Response.ok().entity(TodoService.convertToJson(todoById)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TodoJson> getAll() {
        return todoService.findAllTodos().stream().map(TodoService::convertToJson).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodo(TodoJson todoJson) {
        TodoJson createTodo;
        try {
            createTodo = TodoService.convertToJson(todoService.createTodo(todoJson));
        } catch (IllegalStateException e) {
            return buildPreconditionFailedResponse(e.getMessage());
        }
        return Response.ok().entity(createTodo).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTodo(TodoJson todoJson) {
        TodoJson updatedTodo;
        try {
            updatedTodo = TodoService.convertToJson(todoService.updateTodo(todoJson));
        } catch (IllegalStateException e) {
            return buildPreconditionFailedResponse(e.getMessage());
        } catch (NotFoundException e) {
            return buildNotFoundResponse(e.getMessage());
        }
        return Response.ok().entity(updatedTodo).build();
    }

    @DELETE
    @Path("{todoId}")
    public Response deleteTodo(@PathParam("todoId") String todoId) {
        try {
            todoService.deleteTodoById(todoId);
        } catch (IllegalStateException e) {
            return buildPreconditionFailedResponse(e.getMessage());
        } catch (NotFoundException e) {
            String message = e.getMessage();
            return buildNotFoundResponse(message);
        }
        return Response.ok().build();
    }

    private Response buildPreconditionFailedResponse(String message) {
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(message).build();
    }

    private Response buildNotFoundResponse(String message) {
        return Response.status(Response.Status.NOT_FOUND).entity(message).build();
    }

}
