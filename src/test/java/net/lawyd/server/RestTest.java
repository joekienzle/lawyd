package net.lawyd.server;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

public class RestTest {

    @Before
    public void setUp() {
        RestAssured.basePath = "/api";
        RestAssured.port = 7070;
    }

    @Test
    public void getEmptyTodo() throws Exception {
        given().
                accept(MediaType.APPLICATION_JSON).
        when().
                get("/todo").
        then().
                statusCode(Response.Status.OK.getStatusCode());
    }
}
