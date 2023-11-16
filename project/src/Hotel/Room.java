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

public class Room {
    private int roomNumber;
    private String roomType;
    private double price;
    private List<Booking> bookings;

    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        bookings = new ArrayList<>();
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBooked() {
        return !bookings.isEmpty();
    }

    public boolean isCheckedIn() {
        return bookings.stream().anyMatch(Booking::isCheckedIn);
    }

    public boolean isAvailable(LocalDate date) {
        return bookings.stream().noneMatch(booking -> booking.contains(date));
    }

    public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        return bookings.stream().noneMatch(booking -> booking.overlaps(startDate, endDate));
    }

    public void book(LocalDate startDate, LocalDate endDate) {
        bookings.add(new Booking(startDate, endDate));
    }

    public void checkIn() {
        bookings.stream().filter(booking -> !booking.isCheckedIn()).findFirst().ifPresent(Booking::checkIn);
    }

    public double checkOut() {
        Booking booking = bookings.stream().filter(Booking::isCheckedIn).findFirst().orElse(null);
        if (booking == null) {
            return 0;
        } else {
            booking.checkOut();
            return booking.getTotalBill(price);
        }
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + roomType + ", $" + price + ")";
    }
}