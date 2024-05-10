package utilities;

import pojo.pojoClasses.RequestAddPlace;
import pojo.pojoClasses.requestAddPlace.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class TestData {

    public RequestAddPlace requestAddPlace(String name){

        RequestAddPlace addPlace = new RequestAddPlace();
        addPlace.setAccuracy(50);
        addPlace.setName(name);
        addPlace.setLocation(new Location() {{
            setLat(-38.69);
            setLng(-32.72);
        }});
        addPlace.setPhone_number("09067590222");
        addPlace.setAddress("Summer Street, Manila");
        addPlace.setTypes(new ArrayList<String>(Arrays.asList("Type 1", "Type 2", "Type 3")));
        addPlace.setWebsite("https://www.google.com");
        addPlace.setLanguage("French-IN");

        return addPlace;
    }

    public static String placeId(String placeId) {
        String text = "{\n" +
                "  \"place_id\": "+placeId+"\n" +
                "}\n";
        return text;
    }

}
