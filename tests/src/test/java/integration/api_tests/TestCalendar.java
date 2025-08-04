package integration.api_tests;

import integration.Helpers.GenerateUUID;
import integration.objects.Calendar;
import integration.Helpers.GenerateDateTime;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestCalendar extends BaseTest {
    Calendar calendar = new Calendar();

    String startTime = new GenerateDateTime().getStartTime(1).toString();
    String endTime = new GenerateDateTime().getEndTime(1).toString();

    @Test
    public void TestCreateAndFetchMeetings() {
        // create a user and create a meeting to the Calendar
        Response postCreateMeetingResponse = calendar.createUserAndAddAMeeting(
                title,
                description,
                startTime,
                endTime
        );
        assertEquals(title, postCreateMeetingResponse.jsonPath().getString("title"));
        assertEquals(description, postCreateMeetingResponse.jsonPath().getString("description"));

        // fetch needed parameters
        String meetingId = postCreateMeetingResponse.jsonPath().getString("id");
        String calendarId = postCreateMeetingResponse.jsonPath().getString("calendarId");
        String startTime = postCreateMeetingResponse.jsonPath().getString("startTime");
        String endTime = postCreateMeetingResponse.jsonPath().getString("endTime");

        // fetch all meetings
        Response getAllMeetingsResponse = calendar.calendarController.getAllMeetings(
                calendar.userId,
                calendarId,
                startTime,
                endTime
        );
        assertEquals(200, getAllMeetingsResponse.statusCode());
        assertNotNull(getAllMeetingsResponse.jsonPath().getString("$"));

        // fetch a meeting
        Response getAMeetingResponse = calendar.calendarController.getAMeeting(
                meetingId,
                calendar.userId,
                calendarId
        );
        assertEquals(200, getAMeetingResponse.statusCode());
        assertNotNull(getAMeetingResponse.jsonPath().getString("$"));
        assertEquals(meetingId, getAMeetingResponse.jsonPath().getString("id"));
    }

    @Test
    public void TestCreateUpdateAndDeleteMeeting() {
        // create a user and create a meeting to the Calendar
        Response postCreateMeetingResponse = calendar.createUserAndAddAMeeting(
                title,
                description,
                startTime,
                endTime
        );
        assertNotNull(postCreateMeetingResponse.jsonPath().getString("$"));

        // fetch needed parameters and testdata
        String meetingId = postCreateMeetingResponse.jsonPath().getString("id");
        String calendarId = postCreateMeetingResponse.jsonPath().getString("calendarId");
        String titleToUpdate = title + title;
        String descriptionToUpdate = description + description;
        String startTime = postCreateMeetingResponse.jsonPath().getString("startTime");
        String endTime = postCreateMeetingResponse.jsonPath().getString("endTime");

        // update meeting
        Response putUpdateMeetingResponse = calendar.calendarController.putUpdateMeeting(
                meetingId,
                calendar.userId,
                calendarId,
                titleToUpdate,
                descriptionToUpdate,
                startTime,
                endTime
        );
        System.out.println(putUpdateMeetingResponse.then().log().all());
        assertEquals(200, putUpdateMeetingResponse.statusCode());
        assertEquals(titleToUpdate, putUpdateMeetingResponse.jsonPath().getString("title"));
        assertEquals(descriptionToUpdate, putUpdateMeetingResponse.jsonPath().getString("description"));

        // delete meeting
        Response deleteMeetingResponse = calendar.calendarController.deleteMeeting(
                meetingId,
                calendar.userId,
                calendarId
        );
        assertEquals(204, deleteMeetingResponse.statusCode());

        // check that meeting does not exist anymore after delete
        Response getAMeetingResponse = calendar.calendarController.getAMeeting(
                meetingId,
                calendar.userId,
                calendarId
        );
        assertEquals(404, getAMeetingResponse.statusCode());

    }

    @Test
    public void TestCreateMeetingAndFetchTimeSlots() {
        // create a user and create a meeting to the Calendar
        Response postCreateMeetingResponse = calendar.createUserAndAddAMeeting(
                title,
                description,
                startTime,
                endTime
        );
        assertEquals(title, postCreateMeetingResponse.jsonPath().getString("title"));

        // fetch needed parameters and testdata
        String calendarId = postCreateMeetingResponse.jsonPath().getString("calendarId");
        String startTime = postCreateMeetingResponse.jsonPath().getString("startTime");
        String endTime = postCreateMeetingResponse.jsonPath().getString("endTime");
        int slotDuration = 30;

        // fetch empty time slots for slotDuration with existing meeting
        Response getMeetingSlotResponse = calendar.calendarController.getMeetingSlots(
                calendar.userId,
                calendarId,
                startTime,
                endTime,
                slotDuration
        );
        System.out.println(getMeetingSlotResponse.then().log().all());
        assertEquals(200, getMeetingSlotResponse.statusCode());
        assertNull(getMeetingSlotResponse.jsonPath().getString("slots[0]")); // empty slot

        // fetch free time slots for slotDuration without existing meetings
        String extendedEndTime = new GenerateDateTime().getEndTime(3).toString();
        Response getFreeMeetingSlotResponse = calendar.calendarController.getMeetingSlots(
                calendar.userId,
                calendarId,
                startTime,
                extendedEndTime,
                slotDuration
        );
        System.out.println(getFreeMeetingSlotResponse.then().log().all());
        assertEquals(200, getFreeMeetingSlotResponse.statusCode());
        assertNotNull(getFreeMeetingSlotResponse.jsonPath().getString("slots[0]"));
    }

    @Test
    public void TestMeetingOrUserOrCalendarNotFound() {
        // generate testdata
        String nonExistingMeetingId = new GenerateUUID().getUUID();
        String nonExistingUserId = new GenerateUUID().getUUID();
        String nonExistingCalendarId = new GenerateUUID().getUUID();


        // fetch non-existing meeting, user and calendar,
        Response getAMeetingResponse = calendar.calendarController.getAMeeting(
                nonExistingMeetingId,
                nonExistingUserId,
                nonExistingCalendarId
        );
        assertEquals(404, getAMeetingResponse.statusCode());

        // internal server error / invalid request
        Response getAMeetingInvalidRequestResponse = calendar.calendarController.getAMeeting(
                null,
                nonExistingUserId,
                nonExistingCalendarId
        );
        assertEquals(500, getAMeetingInvalidRequestResponse.statusCode());
    }
}