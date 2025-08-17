package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Room;
import model.RoomComponent;
import model.decorator.MinibarDecorator;
import model.decorator.SpaDecorator;

import java.time.LocalDate;
import java.util.List;

public class RoomManagementController {
    private RoomController roomController = new RoomController();

    public boolean addRoom(String number, String type, String status, double basePrice,
                           boolean hasSpa, boolean hasMinibar) {
        // Check if room number already exists
        if (roomController.isRoomNumberTaken(number)) {
            return false;
        }

        // Create base room
        Room room = new Room(number.trim(), type, status, basePrice);

        // Apply decorators and build description
        RoomComponent decoratedRoom = room;
        String description = type + " Room";

        if (hasSpa) {
            decoratedRoom = new SpaDecorator(decoratedRoom);
            description += ", Spa Access";
        }

        if (hasMinibar) {
            decoratedRoom = new MinibarDecorator(decoratedRoom);
            description += ", Minibar";
        }

        // Set the final description on the base room
        room.setDescription(description);

        // Save the base room
        return roomController.createRoom(room);
    }

    public boolean updateRoom(RoomComponent selected, String number, String type, String status,
                              double basePrice, boolean hasSpa, boolean hasMinibar) {
        // Check if room number is taken by another room
        if (roomController.isRoomNumberTaken(number, selected.getId())) {
            return false;
        }

        // Get the base room (unwrap decorators)
        Room baseRoom = getBaseRoom(selected);
        baseRoom.setNumber(number.trim());
        baseRoom.setType(type);
        baseRoom.setStatus(status);
        baseRoom.setBasePrice(basePrice);

        // Apply decorators and build description
        RoomComponent decoratedRoom = baseRoom;
        String description = type + " Room";

        if (hasSpa) {
            decoratedRoom = new SpaDecorator(decoratedRoom);
            description += ", Spa Access";
        }

        if (hasMinibar) {
            decoratedRoom = new MinibarDecorator(decoratedRoom);
            description += ", Minibar";
        }

        // Set the final description on the base room
        baseRoom.setDescription(description);

        return roomController.updateRoom(baseRoom);
    }

    public boolean deleteRoom(int roomId) {
        return roomController.deleteRoom(roomId);
    }

    public List<Room> searchByDateRange(LocalDate checkin, LocalDate checkout) {
        return roomController.getAvailableRoomsForDateRange(checkin, checkout);
    }

    public List<Room> filterByType(String selectedType) {
        if (selectedType != null && !selectedType.equals("All")) {
            return roomController.getRoomsByType(selectedType);
        }
        return roomController.getAllRooms();
    }

    public List<Room> filterByStatus(String selectedStatus) {
        if (selectedStatus == null || selectedStatus.equals("All")) {
            return roomController.getAllRooms();
        } else if (selectedStatus.equals("Available")) {
            return roomController.getAvailableRooms();
        } else {
            // Filter by specific status
            List<Room> allRooms = roomController.getAllRooms();
            return allRooms.stream()
                    .filter(room -> room.getStatus().equals(selectedStatus))
                    .toList();
        }
    }

    public List<String> getAllRoomTypes() {
        return roomController.getAllRoomTypes();
    }

    public List<Room> getAllRooms() {
        return roomController.getAllRooms();
    }

    public List<Room> getAvailableRooms() {
        return roomController.getAvailableRooms();
    }

    public int getTotalRoomsCount() {
        return roomController.getTotalRoomsCount();
    }

    public int getAvailableRoomsCount() {
        return roomController.getAvailableRoomsCount();
    }

    public int getOccupiedRoomsCount() {
        return roomController.getOccupiedRoomsCount();
    }

    public double calculateTotalPrice(RoomComponent room) {
        // Get base price
        Room baseRoom = getBaseRoom(room);
        double totalPrice = baseRoom.getBasePrice();

        // Check for decorators in description and add their costs
        String description = room.getDescription();
        if (description != null) {
            if (description.contains("Spa Access")) {
                totalPrice += 50.0; // Spa decorator cost
            }
            if (description.contains("Minibar")) {
                totalPrice += 25.0; // Minibar decorator cost
            }
        }

        return totalPrice;
    }

    public Room getBaseRoom(RoomComponent roomComponent) {
        // Unwrap decorators to get the base Room object
        while (!(roomComponent instanceof Room)) {
            if (roomComponent instanceof SpaDecorator) {
                roomComponent = ((SpaDecorator) roomComponent).getWrappedRoom();
            } else if (roomComponent instanceof MinibarDecorator) {
                roomComponent = ((MinibarDecorator) roomComponent).getWrappedRoom();
            } else {
                break; // Unknown decorator type
            }
        }
        return (Room) roomComponent;
    }

    public boolean validateInput(String number, String type, String status, String priceText) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }

        if (type == null) {
            return false;
        }

        if (status == null) {
            return false;
        }

        if (priceText == null || priceText.trim().isEmpty()) {
            return false;
        }

        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean isValidPrice(String priceText) {
        try {
            double price = Double.parseDouble(priceText);
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidDateRange(LocalDate checkin, LocalDate checkout) {
        return checkin != null && checkout != null && !checkin.isAfter(checkout);
    }
}