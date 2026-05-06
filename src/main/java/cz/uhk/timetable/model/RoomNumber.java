package cz.uhk.timetable.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalTime;

/**
 * POJO Timetable roomNumber
 */

public class RoomNumber {
    /** Building ID, ie. J, A, B etc. */
    @SerializedName("zkrBudovy")
    private String buildingID;
    /** Room number J22, J20, AULA etc. */
    @SerializedName("cisloMistnosti")
    private String roomID;

    public RoomNumber(String buildingID, String roomID) {
        this.buildingID = buildingID;
        this.roomID = roomID;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @Override
    public String toString() {
        return "RoomNumber{" +
                "buildingID='" + buildingID + '\'' +
                ", roomID='" + roomID + '\'' +
                '}';
    }
}
