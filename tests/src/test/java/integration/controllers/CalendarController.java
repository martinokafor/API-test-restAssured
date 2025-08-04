package integration.controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import static io.restassured.RestAssured.given;


public class CalendarController {
    String baseUrl = "http://localhost:8082/";

    public Response postCreateAMeeting(
            String userId,
            String title,
            String description,
            String startTime,
            String endTime,
            String calendarId) {
        JSONObject body = new JSONObject();
        body.put("id", "");
        body.put("title", title);
        body.put("description", description);
        body.put("startTime", startTime);
        body.put("endTime", endTime);
        body.put("location", "Berlin");
        body.put("calendarId", calendarId);

        Response response =
                given().
                        headers("Content-type", "application/json").
                        queryParam("userId", userId).
                        body(body.toJSONString()).
                        when().
                        post(baseUrl + "meeting").
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response getAllMeetings(
            String userId,
            String calendarId,
            String startTime,
            String endTime
    ) {
        Response response =
                given().
                        queryParam("userId", userId).
                        queryParam("calendarId", calendarId).
                        queryParam("from", startTime).
                        queryParam("to", endTime).
                        queryParam("page", 0).
                        queryParam("size", 20).
                        when().
                        get(baseUrl + "meeting").
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response getAMeeting(
            String id,
            String userId,
            String calendarId
    ) {
        Response response =
                given().
                        queryParam("userId", userId).
                        queryParam("calendarId", calendarId).
                        when().
                        get(baseUrl + "meeting/" + id).
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response putUpdateMeeting(
            String id,
            String userId,
            String calendarId,
            String title,
            String description,
            String startTime,
            String endTime
    ) {
        JSONObject body = new JSONObject();
        body.put("id", id);
        body.put("title", title);
        body.put("description", description);
        body.put("calendarId", calendarId);
        body.put("startTime", startTime);
        body.put("endTime", endTime);

        Response response =
                given().
                        headers("Content-type", "application/json").
                        queryParam("userId", userId).
                        body(body.toJSONString()).
                        when().
                        put(baseUrl + "meeting/" + id).
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }

    public Response deleteMeeting(String id, String userId, String calendarId) {
        Response response =
                given().
                        headers("Content-type", "application/json").
                        queryParam("userId", userId).
                        queryParam("calendarId", calendarId).
                        when().
                        delete(baseUrl + "meeting/" + id);
        return response;
    }

    public Response getMeetingSlots(
            String userId,
            String calendarId,
            String startTime,
            String endTime,
            int slotDuration
    ) {
        Response response =
                given().
                        queryParam("userId", userId).
                        queryParam("calendarId", calendarId).
                        queryParam("from", startTime).
                        queryParam("to", endTime).
                        queryParam("slotDuration", slotDuration).
                        queryParam("page", 0).
                        queryParam("size", 20).
                        when().
                        get(baseUrl + "meeting/slots").
                        then().
                        contentType(ContentType.JSON).extract().response();
        return response;
    }
}
