package cz.uhk.timetable.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.timetable.model.LocationTimetable;
import cz.uhk.timetable.model.RoomNumber;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StagRoomProvider {
    private static final String ROOM_URL = "https://stag-demo.uhk.cz/ws/services/rest2/mistnost/getMistnostiInfo?zkrBudovy=%s&pracoviste=%%25&typ=U&outputFormat=JSON&cisloMistnosti=%%25";
    private final Gson gson = new Gson();

    /**
     * Fetches list of rooms for a specific building
     */
    public List<RoomNumber> getRooms(String building) {
        try {
            var url = new URL(String.format(ROOM_URL, building));
            var reader = new InputStreamReader(url.openStream());

            Map<String, List<RoomNumber>> response = gson.fromJson(reader,
                    new TypeToken<Map<String, List<RoomNumber>>>(){}.getType());

            return response.getOrDefault("mistnostInfo", new ArrayList<>());
        } catch (IOException e) {
            System.err.println("Error fetching rooms for building: " + building);
            return new ArrayList<>();
        }
    }
}