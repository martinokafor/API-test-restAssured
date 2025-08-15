package integration.controllers;

import static io.restassured.RestAssured.given;
import org.json.simple.JSONObject;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;


public class UserController {

    String baseUrl = "http://localhost:8080/";

    public Response postUser(String name, String email, String calendarId) {

        JSONObject body = new JSONObject();
        body.put("id", "");
        body.put("name", name);
        body.put("email", email);
        List<String> calendarIds = new ArrayList<>();
        calendarIds.add(calendarId);
        body.put("calendarIds", calendarIds);

        Response response =
                given().
                        headers("Content-type", "application/json").
                        body(body.toJSONString()).
                        when().
                        post(baseUrl + "api/users").
                        then().
                        contentType(ContentType.JSON).extract().response();
        System.out.println(response.then().log().all());
        return response;
    }

    public Response getAllUsers() {
        Response response =
                given().
                        queryParam("page", 0).
                        queryParam("size", 20).
                        when().
                        get(baseUrl + "api/users").
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;

    }

    public Response postCalendarToUser(String userId, String calendarId) {
        Response response =
                given().
                        headers("Content-type", "application/json").
                        when().
                        post(baseUrl + "api/users/" + userId + "/calendars/" + calendarId).
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response getUserById(String id) {
        Response response =
                given().
                        when().
                        get(baseUrl + "api/users/" + id).
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;

    }

    public Response putUpdateUser(String userId, String name, String email) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("email", email);

        Response response =
                given().
                        headers("Content-type", "application/json").
                        body(body.toJSONString()).
                        when().
                        put(baseUrl + "api/users/" + userId).
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response deleteUser(String userId) {
        Response response =
                given().
                        headers("Content-type", "application/json").
                        when().
                        delete(baseUrl + "api/users/" + userId);
        return response;
    }

}
