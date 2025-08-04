package integration.objects;

import integration.controllers.UserController;
import io.restassured.response.Response;


public class User {
    public UserController userController = new UserController();

    public Response createUserAndAddCalendar(
            String name,
            String email,
            String calendarOneId,
            String calendarTwoId
    ) {
        // create user
        Response createUserResponse = userController.postUser(name, email, calendarOneId);
        System.out.println(createUserResponse.then().log().all());
        createUserResponse.then().statusCode(201);
        String userId = createUserResponse.jsonPath().getString("id");
        System.out.println(userId);

        // add calendar to user
        Response addCalendarResponse = userController.postCalendarToUser(userId, calendarTwoId);
        addCalendarResponse.then().statusCode(200);
        return addCalendarResponse;
    }

    public Response createAndUpdateUser(
            String name,
            String email,
            String calendarOneId,
            String nameToUpdate
    ) {
        // create user
        Response createUserResponse = userController.postUser(name, email, calendarOneId);
        createUserResponse.then().statusCode(201);
        String userId = createUserResponse.jsonPath().getString("id");
        System.out.println(userId);

        // update user
        Response updateUserResponse = userController.putUpdateUser(userId, nameToUpdate, email);
        updateUserResponse.then().statusCode(200);
        return updateUserResponse;
    }
}
