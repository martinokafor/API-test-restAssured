package integration.objects;

import integration.controllers.CalendarController;
import integration.api_tests.BaseTest;
import integration.controllers.UserController;
import io.restassured.response.Response;


public class Calendar {
    BaseTest baseTest = new BaseTest();
    public UserController userController = new UserController();
    public CalendarController calendarController = new CalendarController();

    public String userId;

    public Response createUserAndAddAMeeting(
            String title,
            String description,
            String startTime,
            String endTime
    ) {
        // create user with calendar
        Response createUserResponse = userController.postUser(
                baseTest.name,
                baseTest.email,
                baseTest.uuid
        );
        createUserResponse.then().statusCode(201);
        System.out.println(createUserResponse.then().log().all());
        userId = createUserResponse.jsonPath().getString("id");
        System.out.println(userId);

        // create a meeting
        Response postCreateMeetingResponse = calendarController.postCreateAMeeting(
                userId,
                title,
                description,
                startTime,
                endTime,
                baseTest.uuid
        );
        postCreateMeetingResponse.then().statusCode(201);
        System.out.println(postCreateMeetingResponse.then().log().all());
        return  postCreateMeetingResponse;
    }
}
