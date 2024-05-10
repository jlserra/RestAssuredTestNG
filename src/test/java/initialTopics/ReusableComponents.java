package initialTopics;

import io.restassured.path.json.JsonPath;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ReusableComponents {

    public static JsonPath stringToJson(String response){
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath;
    }

    public static String getRandomNumber(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Convert to milliseconds
        Instant instant = currentDateTime.toInstant(ZoneOffset.UTC);
        long milliseconds = instant.toEpochMilli();
        return String.valueOf(milliseconds);
    }
}
