package net.lawyd.server;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

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
    public void getEmptyTodo() throws Exception {
        given().
                accept(MediaType.APPLICATION_JSON).
        when().
                get().
        then().
                statusCode(Response.Status.OK.getStatusCode());
    }
}
