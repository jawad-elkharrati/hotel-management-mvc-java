package model;

public interface RoomComponent {
    double getPrice();
    String getDescription();
    String getNumber();
    String getType();
    String getStatus();
    int getId();
    boolean isAvailable();
    boolean isOccupied();
}
