package net.lawyd.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.lawyd.server.persistence.Todo;
import net.lawyd.server.rest.model.TodoJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.basePath = "/todo";
        RestAssured.port = port;
    }

    @Test
    public void crud() throws Exception {
        Gson gson = new Gson();

        String getInitalTodosResponse =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        Type todoListType = new TypeToken<Collection<Todo>>() {
        }.getType();
        List<Todo> todos = gson.fromJson(getInitalTodosResponse, todoListType);
        assertNotNull(todos);
        int initalSize = todos.size();

        String name1 = "My todo";
        String description1 = "Super important";
        int priority1 = 1;
        String createResponse1 =
                given().contentType(ContentType.JSON).body(gson.toJson(new TodoJson(null, name1, description1, priority1))).accept(MediaType.APPLICATION_JSON).
                        when().post().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        TodoJson todoJson1 = gson.fromJson(createResponse1, TodoJson.class);
        assertNotNull(todoJson1);
        assertNotNull(todoJson1.getId());
        assertEquals(name1, todoJson1.getName());
        assertEquals(description1, todoJson1.getDescription());
        assertEquals(priority1, todoJson1.getPriority());

        String getCreatedTodosResponse1 =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        List<Todo> todosAfterCreate1 = gson.fromJson(getCreatedTodosResponse1, todoListType);
        assertNotNull(todosAfterCreate1);
        assertEquals(initalSize + 1, todosAfterCreate1.size());

        String name2 = "My unimportant todo";
        String description2 = "Not so important";
        int priority2 = 2;

        String createResponse2 =
                given().contentType(ContentType.JSON).body(gson.toJson(new TodoJson(null, name2, description2, priority2))).accept(MediaType.APPLICATION_JSON).
                        when().post().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        TodoJson todoJson2 = gson.fromJson(createResponse2, TodoJson.class);
        assertNotNull(todoJson2);
        assertNotNull(todoJson2.getId());
        assertEquals(name2, todoJson2.getName());
        assertEquals(description2, todoJson2.getDescription());
        assertEquals(priority2, todoJson2.getPriority());

        String getCreatedTodosResponse2 =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        List<Todo> todosAfterCreate2 = gson.fromJson(getCreatedTodosResponse2, todoListType);
        assertNotNull(todosAfterCreate2);
        assertEquals(initalSize + 2, todosAfterCreate2.size());

        final boolean[] todoFound = {false, false};
        todosAfterCreate2.forEach(todo -> {
            if (todo.getId().equals(todoJson1.getId())) {
                todoFound[0] = true;
            }
            if (todo.getId().equals(todoJson2.getId())) {
                todoFound[1] = true;
            }
        });
        assertTrue(todoFound[0]);
        assertTrue(todoFound[1]);

        String getTodo2 =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get("/" + todoJson2.getId()).
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        TodoJson readTodoJson2 = gson.fromJson(getTodo2, TodoJson.class);
        assertNotNull(readTodoJson2);
        assertEquals(todoJson2.getId(), readTodoJson2.getId());
        assertEquals(name2, readTodoJson2.getName());
        assertEquals(description2, readTodoJson2.getDescription());
        assertEquals(priority2, readTodoJson2.getPriority());

        String updatedName = "My updated name";
        String updatedDescription = "My updated description";
        int updatedPriority = 3;
        given().contentType(ContentType.JSON).body(gson.toJson(new TodoJson(todoJson2.getId(), updatedName, updatedDescription, updatedPriority))).accept(MediaType.APPLICATION_JSON).
                when().put().
                then().statusCode(Response.Status.OK.getStatusCode());

        String getUpdatedTodo2 =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get("/" + todoJson2.getId()).
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        TodoJson updatedTodoJson2 = gson.fromJson(getUpdatedTodo2, TodoJson.class);
        assertEquals(todoJson2.getId(), updatedTodoJson2.getId());
        assertEquals(updatedName, updatedTodoJson2.getName());
        assertEquals(updatedDescription, updatedTodoJson2.getDescription());
        assertEquals(updatedPriority, updatedTodoJson2.getPriority());

        String getUpdatedTodosResponse =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        List<Todo> todosAfterUpdate = gson.fromJson(getUpdatedTodosResponse, todoListType);
        assertNotNull(todosAfterUpdate);
        assertEquals(initalSize + 2, todosAfterUpdate.size());

        given().accept(MediaType.APPLICATION_JSON).
                when().delete("/" + todoJson2.getId()).
                then().statusCode(Response.Status.OK.getStatusCode());

        String getDeletedTodosResponse =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();
        List<Todo> todosAfterDelete = gson.fromJson(getDeletedTodosResponse, todoListType);
        assertNotNull(todosAfterDelete);
        assertEquals(initalSize + 1, todosAfterDelete.size());
    }

    @Test
    public void crudErrorCases() {
        given().accept(MediaType.APPLICATION_JSON).
                when().get("/WRONG").
                then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given().accept(MediaType.APPLICATION_JSON).contentType(ContentType.JSON).
                when().post().
                then().statusCode(Response.Status.PRECONDITION_FAILED.getStatusCode());

        given().accept(MediaType.APPLICATION_JSON).contentType(ContentType.JSON).
                when().put().
                then().statusCode(Response.Status.PRECONDITION_FAILED.getStatusCode());

        Gson gson = new Gson();

        given().accept(MediaType.APPLICATION_JSON).contentType(ContentType.JSON).body(gson.toJson(new TodoJson("", "", "", 1))).
                when().put().
                then().statusCode(Response.Status.PRECONDITION_FAILED.getStatusCode());

        given().accept(MediaType.APPLICATION_JSON).contentType(ContentType.JSON).body(gson.toJson(new TodoJson("WRONG", "", "", 1))).
                when().put().
                then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given().accept(MediaType.APPLICATION_JSON).
                when().delete("/WRONG").
                then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

}
