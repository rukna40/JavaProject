package Hotel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Hotel{
    private List<Room> rooms;

    public Hotel() {
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAvailableRooms(LocalDate date) {
        return rooms.stream().filter(room -> room.isAvailable(date)).collect(Collectors.toList());
    }

    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        return rooms.stream().filter(room -> room.isAvailable(startDate, endDate)).collect(Collectors.toList());
    }

    public Room getRoom(int roomNumber) {
        return rooms.stream().filter(room -> room.getRoomNumber() == roomNumber).findFirst().orElse(null);
    }
}