package model;

public class Room implements RoomComponent {
    private int id;
    private String number;
    private String type;
    private String status;
    private double basePrice;
    private String description;

    // Constructor
    public Room() {}
    public Room(String number, String type, String status, double basePrice) {
        this.number = number;
        this.type = type;
        this.status = status;
        this.basePrice = basePrice;
        this.description = type + " Room";
    }

    // Getters and setters
    @Override
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    @Override
    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    @Override
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public double getPrice() { return basePrice; }

    // Utility methods
    @Override
    public boolean isAvailable() {
        return "available".equalsIgnoreCase(status);
    }

    @Override
    public boolean isOccupied() {
        return "occupied".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }
}