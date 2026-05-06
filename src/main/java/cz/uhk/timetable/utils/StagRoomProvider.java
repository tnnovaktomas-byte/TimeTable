package cz.uhk.timetable.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.uhk.timetable.model.LocationTimetable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;

public class StagRoomProvider implements ITimetableProvider {
    //Static jen jedna instance, final že se nemění
    private static final String ROOM_URL="https://stag-demo.uhk.cz/ws/services/rest2/mistnost/getMistnostiInfo?zkrBudovy=%S&pracoviste=%%25&typ=U&outputFormat=JSON&cisloMistnosti=%%25";
    private final Gson gson = new Gson();

    public StagRoomProvider() {
        gson = new GsonBuilder().registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()).create();
    }

    @Override
    public LocationTimetable readTimetable(String building, String room) {

        try {
            var url = new URL(ROOM_URL.formatted(building, room));
            var reader = new InputStreamReader(url.openStream());

            return gson.fromJson(reader, LocationTimetable.class);
        } catch (MalformedURLException exception) {
            System.out.println("Wrong URL");
            throw new RuntimeException(exception);
        } catch (IOException e) {
            System.out.println("IO Error during timetable reading");
            throw new RuntimeException(e);
        }
    }
}
