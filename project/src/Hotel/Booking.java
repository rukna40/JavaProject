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

public class Booking {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean checkedIn;
    private boolean checkedOut;

    public Booking(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlaps(LocalDate startDate, LocalDate endDate) {
        return !this.endDate.isBefore(startDate) && !this.startDate.isAfter(endDate);
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void checkIn() {
        checkedIn = true;
    }

    public void checkOut() {
        checkedOut = true;
    }

    public double getTotalBill(double price) {
        long nights = startDate.until(endDate).getDays();
        return nights * price;
    }
}
