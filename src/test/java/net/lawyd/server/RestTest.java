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
    public void crudTodo() throws Exception {
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

        String name = "My todo";
        String description = "Super important";
        int priority = 1;
        String createResponse =
                given().contentType(ContentType.JSON).body(gson.toJson(new TodoJson(null, name, description, priority))).accept(MediaType.APPLICATION_JSON).
                        when().post().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        TodoJson todoJson = gson.fromJson(createResponse, TodoJson.class);
        assertNotNull(todoJson);
        assertNotNull(todoJson.getId());
        assertEquals(name, todoJson.getName());
        assertEquals(description, todoJson.getDescription());
        assertEquals(priority, todoJson.getPriority());

        String getCreatedTodosResponse =
                given().accept(MediaType.APPLICATION_JSON).
                        when().get().
                        then().statusCode(Response.Status.OK.getStatusCode()).extract().body().asString();

        List<Todo> todosAfterCreate = gson.fromJson(getCreatedTodosResponse, todoListType);
        assertNotNull(todosAfterCreate);
        assertEquals(initalSize + 1, todosAfterCreate.size());
        final boolean[] todoFound = {false};
        todosAfterCreate.forEach(todo -> {
            if (todo.getId().equals(todoJson.getId())) {
                todoFound[0] = true;
            }
        });
        assertTrue(todoFound[0]);
    }
}
