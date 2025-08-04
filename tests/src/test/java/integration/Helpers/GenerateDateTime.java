package integration.Helpers;

import java.time.LocalDateTime;


public class GenerateDateTime {

    public LocalDateTime getStartTime(int timeDiff) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusHours(timeDiff);
    }

    public LocalDateTime getEndTime(int timeDiff) {
        LocalDateTime startTime = getStartTime(timeDiff);
        return startTime.plusHours(timeDiff);
    }
}
