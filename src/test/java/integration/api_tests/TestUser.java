package integration.api_tests;

import integration.Helpers.GenerateUUID;
import integration.objects.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestUser extends BaseTest {

    User user = new User();

    @Test
    public void TestCreateAndFetchUsers() {
        // create a user
        Response postUserResponse = user.userController.postUser(
                name,
                email,
                uuid
        );
        assertEquals(201, postUserResponse.statusCode());
        assertEquals(email, postUserResponse.jsonPath().getString("email"));
        assertEquals(name, postUserResponse.jsonPath().getString("name"));
        assertEquals(uuid, postUserResponse.jsonPath().getString("calendarIds[0]"));
        String userId = postUserResponse.jsonPath().getString("id");

        // fetch all created users
        Response getAllUsersResponse = user.userController.getAllUsers();
        assertEquals(200, getAllUsersResponse.statusCode());
        assertNotNull(getAllUsersResponse.jsonPath().getString("$"));

        // fetch a created user by id
        Response getUserByIdResponse = user.userController.getUserById(userId);
        assertEquals(200, getUserByIdResponse.statusCode());
        assertEquals(email, getUserByIdResponse.jsonPath().getString("email"));
        assertEquals(name, getUserByIdResponse.jsonPath().getString("name"));
    }

    @Test
    public void TestAddCalendarToUser() {
        // create and add calendar to user
        String calendarTwoId = new GenerateUUID().getUUID();
        Response postCalendarToUserResponse = user.createUserAndAddCalendar(
                name,
                email,
                uuid,
                calendarTwoId
        );
        List calendarIds = new ArrayList<>();
        calendarIds.add(uuid);
        calendarIds.add(calendarTwoId);
        assertEquals(
                calendarIds.toString(),
                postCalendarToUserResponse.jsonPath().getString("calendarIds"));
    }

    @Test
    public void TestUpdateAndDeleteUser() {
        // create and update user
        String nameToUpdate = name + name;
        Response createAndUpdateUserResponse = user.createAndUpdateUser(
                name,
                email,
                uuid,
                nameToUpdate
        );
        assertEquals(nameToUpdate, createAndUpdateUserResponse.jsonPath().getString("name"));

        // delete user
        String userId = createAndUpdateUserResponse.jsonPath().getString("id");
        System.out.println(userId);
        Response deleteUserResponse = user.userController.deleteUser(
                userId
        );
        assertEquals(204, deleteUserResponse.statusCode());

        // check user does not exist anymore in database
        Response getUserByIdResponse = user.userController.getUserById(userId);
        assertEquals(404, getUserByIdResponse.statusCode());
    }

    @Test
    public void TestUserNotFound() {
        // generate needed testdata
        String nonExistingUserId = new GenerateUUID().getUUID();

        // fetch non-existing user
        Response getUserByIdResponse = user.userController.getUserById(
                nonExistingUserId
        );
        assertEquals(404, getUserByIdResponse.statusCode());

        //  internal server error / invalid request
        Response getUserByIdInvalidRequestResponse = user.userController.getUserById(
                null
        );
        assertEquals(500, getUserByIdInvalidRequestResponse.statusCode());
    }
}
