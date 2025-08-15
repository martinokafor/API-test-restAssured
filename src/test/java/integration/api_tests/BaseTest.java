package integration.api_tests;

import integration.Helpers.GenerateUUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


public class BaseTest {
    // testdata
    public String uuid = new GenerateUUID().getUUID();
    public String title = "meeting" + uuid.substring(0, 8);
    public String description = "interview" + uuid.substring(0, 8);
    public String email = "martinok" + uuid.substring(0, 5) + "@mailinator.com";
    public String name = "martin" + uuid.substring(0, 5);

    @BeforeEach
    public void setUpTest(TestInfo testInfo) {
        System.out.println("Starting" + " " + testInfo.getDisplayName());
    }

    @AfterEach
    public void tearDownTest(TestInfo testInfo) {
        System.out.println("Ending" + " " + testInfo.getDisplayName());
    }
}