package sample.models;
import java.util.List;

public class Room {

    public int roomID ;
    public String name;

    public Room(int roomID, String name) {
        this.roomID = roomID;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", name='" + name + '\'' +
                '}';
    }

    public int getRoomID() {
        return roomID;
    }

    public String getName() {
        return name;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
