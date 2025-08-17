package controller;

import model.Guest;
import model.Reservation;
import model.Room;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationManagementController {
    private ReservationController reservationController = new ReservationController();
    private GuestController guestController = new GuestController();
    private RoomController roomController = new RoomController();

    public boolean createReservation(Guest guest, Room room, LocalDate checkin, LocalDate checkout) {
        // Check room availability one more time
        if (!reservationController.isRoomAvailable(room.getId(), checkin, checkout)) {
            return false;
        }

        // Create reservation using the constructor that matches your Reservation model
        Reservation reservation = new Reservation(guest, room, checkin, checkout);

        return reservationController.createReservation(reservation);
    }

    public boolean updateReservation(Reservation reservation, Guest guest, Room room, LocalDate checkin, LocalDate checkout) {
        // Update the reservation
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckinDate(checkin);
        reservation.setCheckoutDate(checkout);

        return reservationController.updateReservation(reservation);
    }

    public boolean deleteReservation(int reservationId) {
        return reservationController.deleteReservation(reservationId);
    }

    public List<Reservation> searchReservations(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllReservations();
        }

        return getAllReservations().stream()
                .filter(r -> r.getGuest() != null &&
                        r.getGuest().getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Reservation> filterReservations(String filterType) {
        switch (filterType) {
            case "Active":
                return getAllReservations().stream()
                        .filter(r -> r.isActive())
                        .collect(Collectors.toList());
            case "Future":
                return getAllReservations().stream()
                        .filter(r -> r.getCheckinDate().isAfter(LocalDate.now()))
                        .collect(Collectors.toList());
            case "Past":
                return getAllReservations().stream()
                        .filter(r -> r.getCheckoutDate().isBefore(LocalDate.now()))
                        .collect(Collectors.toList());
            default:
                return getAllReservations();
        }
    }

    public boolean isRoomAvailable(int roomId, LocalDate checkin, LocalDate checkout) {
        return reservationController.isRoomAvailable(roomId, checkin, checkout);
    }

    public ValidationResult validateReservation(Guest guest, Room room, LocalDate checkin, LocalDate checkout) {
        if (guest == null) {
            return new ValidationResult(false, "Please select a guest!");
        }
        if (room == null) {
            return new ValidationResult(false, "Please select a room!");
        }
        if (checkin == null) {
            return new ValidationResult(false, "Please select check-in date!");
        }
        if (checkout == null) {
            return new ValidationResult(false, "Please select check-out date!");
        }
        if (checkin.isAfter(checkout) || checkin.isEqual(checkout)) {
            return new ValidationResult(false, "Check-out date must be after check-in date!");
        }
        if (checkin.isBefore(LocalDate.now())) {
            return new ValidationResult(false, "Check-in date cannot be in the past!");
        }
        return new ValidationResult(true, "");
    }

    public List<Reservation> getAllReservations() {
        return reservationController.getAllReservations();
    }

    public List<Guest> getAllGuests() {
        return guestController.getAllGuests();
    }

    public List<Room> getAvailableRooms() {
        return roomController.getAvailableRooms();
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}