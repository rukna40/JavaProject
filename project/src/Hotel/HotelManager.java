package Hotel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HotelManager extends Application {

    private Hotel hotel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        hotel = new Hotel();
        
        // ImageView title = new ImageView(new Image("logo.png"));
        // title.setFitWidth(200); // Set the width of the image
        // title.setFitHeight(150);

        Button createRoomButton = new Button("Create a new room");
        createRoomButton.setOnAction(event -> showCreateRoomPopup());

        Button checkAvailabilityButton = new Button("Check availability");
        checkAvailabilityButton.setOnAction(event -> showCheckAvailabilityPopup());

        Button bookRoomButton = new Button("Book a room");
        bookRoomButton.setOnAction(event -> showBookRoomPopup());

        Button checkInButton = new Button("Check-in");
        checkInButton.setOnAction(event -> showCheckInPopup());

        Button checkOutButton = new Button("Check-out");
        checkOutButton.setOnAction(event -> showCheckOutPopup());

        VBox root = new VBox(10,createRoomButton, checkAvailabilityButton, bookRoomButton, checkInButton, checkOutButton);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        root.getStyleClass().add("background");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCreateRoomPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Create a new room");

        Label roomNumberLabel = new Label("Room number:");
        TextField roomNumberField = new TextField();

        Label roomTypeLabel = new Label("Room type:");
        TextField roomTypeField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String roomType = roomTypeField.getText();
            double price = Double.parseDouble(priceField.getText());

            Room room = new Room(roomNumber, roomType, price);
            hotel.addRoom(room);

            popupStage.close();
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.addRow(0, roomNumberLabel, roomNumberField);
        gridPane.addRow(1, roomTypeLabel, roomTypeField);
        gridPane.addRow(2, priceLabel, priceField);
        gridPane.addRow(3, submitButton);

        Scene scene = new Scene(gridPane, 300, 200);
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void showCheckAvailabilityPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Check availability");

        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            List<Room> availableRooms = hotel.getAvailableRooms(date);
            String message = availableRooms.isEmpty() ? "No rooms available" :
                    availableRooms.stream().map(Room::toString).collect(Collectors.joining("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.showAndWait();
        });

        VBox vbox = new VBox(10, datePicker);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void showBookRoomPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Book a room");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            List<Room> availableRooms = hotel.getAvailableRooms(startDate, endDate);
            if (availableRooms.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No rooms available for selected dates");
                alert.showAndWait();
            } else {
                ChoiceDialog<Room> dialog = new ChoiceDialog<>(availableRooms.get(0), availableRooms);
                dialog.setTitle("Select a room");
                dialog.setHeaderText("Available rooms for selected dates:");
                dialog.setContentText("Choose a room:");
                Optional<Room> result = dialog.showAndWait();
                result.ifPresent(room -> {
                    room.book(startDate, endDate);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Room booked successfully");
                    alert.showAndWait();
                });
            }
            popupStage.close();
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.addRow(0, new Label("Start date:"), startDatePicker);
        gridPane.addRow(1, new Label("End date:"), endDatePicker);
        gridPane.addRow(2, submitButton);

        Scene scene = new Scene(gridPane, 300, 200);
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void showCheckInPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Check-in");

        TextField roomNumberField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            Room room = hotel.getRoom(roomNumber);
            if (room == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Room not found");
                alert.showAndWait();
            } else if (!room.isBooked()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Room is not booked");
                alert.showAndWait();
            } else if (room.isCheckedIn()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Room is already checked in");
                alert.showAndWait();
            } else {
                room.checkIn();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Room checked in successfully");
                alert.showAndWait();
                popupStage.close();
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.addRow(0, new Label("Room number:"), roomNumberField);
        gridPane.addRow(1, submitButton);

        Scene scene = new Scene(gridPane, 300, 200);
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void showCheckOutPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Check-out");

        TextField roomNumberField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            Room room = hotel.getRoom(roomNumber);
            if (room == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Room not found");
                alert.showAndWait();
            } else if (!room.isCheckedIn()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Room is not checked in");
                alert.showAndWait();
            } else {
                double bill = room.checkOut();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Room checked out successfully. Total bill: " + bill);
                alert.showAndWait();
                popupStage.close();
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.addRow(0, new Label("Room number:"), roomNumberField);
        gridPane.addRow(1, submitButton);

        Scene scene = new Scene(gridPane, 300, 200);
        popupStage.setScene(scene);
        popupStage.show();
    }
}