package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private Guest guest;
    private Room room;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private LocalDateTime createdAt;

    // Default constructor
    public Reservation() {}


    public Reservation(Guest guest, Room room, LocalDate checkinDate, LocalDate checkoutDate) {
        this.guest = guest;
        this.room = room;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with IDs (for database operations)
    public Reservation(int guestId, int roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        this.guest = GuestFactory.createGuestWithId(guestId);
        this.guest.setId(guestId);
        this.room = new Room();
        this.room.setId(roomId);
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Convenience methods to get IDs (for database operations)
    public int getGuestId() {
        return guest != null ? guest.getId() : 0;
    }

    public int getRoomId() {
        return room != null ? room.getId() : 0;
    }

    // Convenience methods to get related data
    public String getGuestName() {
        return guest != null ? guest.getName() : "";
    }

    public String getRoomNumber() {
        return room != null ? room.getNumber() : "";
    }

    public String getRoomType() {
        return room != null ? room.getType() : "";
    }

    public String getGuestContact() {
        return guest != null ? guest.getContact() : "";
    }

    // Utility methods
    public long getStayDuration() {
        if (checkinDate != null && checkoutDate != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        }
        return 0;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return checkinDate != null && checkoutDate != null &&
                !today.isBefore(checkinDate) && today.isBefore(checkoutDate);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", guest=" + (guest != null ? guest.getName() : "null") +
                ", room=" + (room != null ? room.getNumber() : "null") +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", createdAt=" + createdAt +
                '}';
    }
}