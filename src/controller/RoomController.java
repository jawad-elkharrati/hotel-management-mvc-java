package controller;

import dao.RoomDAO;
import model.Room;
import java.util.List;
import java.time.LocalDate;

public class RoomController {
    private RoomDAO roomDAO;

    public RoomController() {
        this.roomDAO = new RoomDAO();
    }

    /**
     * Create a new room
     * @param room Room object to create
     * @return true if successful, false otherwise
     */
    public boolean createRoom(Room room) {
        return roomDAO.createRoom(room);
    }

    /**
     * Get all rooms
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    /**
     * Get room by ID
     * @param id Room ID
     * @return Room object or null if not found
     */
    public Room getRoomById(int id) {
        return roomDAO.getRoomById(id);
    }

    /**
     * Get room by room number
     * @param number Room number
     * @return Room object or null if not found
     */
    public Room getRoomByNumber(String number) {
        return roomDAO.getRoomByNumber(number);
    }

    /**
     * Get available rooms
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms() {
        return roomDAO.getAvailableRooms();
    }

    /**
     * Get rooms by type
     * @param type Room type
     * @return List of rooms of specified type
     */
    public List<Room> getRoomsByType(String type) {
        return roomDAO.getRoomsByType(type);
    }

    /**
     * Get available rooms by type
     * @param type Room type
     * @return List of available rooms of specified type
     */
    public List<Room> getAvailableRoomsByType(String type) {
        return roomDAO.getAvailableRoomsByType(type);
    }

    /**
     * Get rooms available for specific date range
     * @param checkinDate Check-in date
     * @param checkoutDate Check-out date
     * @return List of available rooms for the date range
     */
    public List<Room> getAvailableRoomsForDateRange(LocalDate checkinDate, LocalDate checkoutDate) {
        return roomDAO.getAvailableRoomsForDateRange(checkinDate, checkoutDate);
    }

    /**
     * Get rooms within a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of rooms within the price range
     */
    public List<Room> getRoomsByPriceRange(double minPrice, double maxPrice) {
        return roomDAO.getRoomsByPriceRange(minPrice, maxPrice);
    }

    /**
     * Get available rooms within a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of available rooms within the price range
     */
    public List<Room> getAvailableRoomsByPriceRange(double minPrice, double maxPrice) {
        return roomDAO.getAvailableRoomsByPriceRange(minPrice, maxPrice);
    }

    /**
     * Update room information
     * @param room Room object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateRoom(Room room) {
        return roomDAO.updateRoom(room);
    }

    /**
     * Update room status
     * @param roomId Room ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRoomStatus(int roomId, String status) {
        return roomDAO.updateRoomStatus(roomId, status);
    }

    /**
     * Update room price
     * @param roomId Room ID
     * @param newPrice New base price
     * @return true if successful, false otherwise
     */
    public boolean updateRoomPrice(int roomId, double newPrice) {
        return roomDAO.updateRoomPrice(roomId, newPrice);
    }

    /**
     * Delete room by ID
     * @param id Room ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRoom(int id) {
        return roomDAO.deleteRoom(id);
    }

    /**
     * Check if room exists
     * @param id Room ID
     * @return true if room exists, false otherwise
     */
    public boolean roomExists(int id) {
        return roomDAO.roomExists(id);
    }

    /**
     * Check if room number is already taken
     * @param number Room number to check
     * @param excludeId Room ID to exclude from check (for updates)
     * @return true if room number exists, false otherwise
     */
    public boolean isRoomNumberTaken(String number, int excludeId) {
        return roomDAO.isRoomNumberTaken(number, excludeId);
    }

    /**
     * Overloaded method for new room creation
     * @param number Room number to check
     * @return true if room number exists, false otherwise
     */
    public boolean isRoomNumberTaken(String number) {
        return roomDAO.isRoomNumberTaken(number);
    }

    /**
     * Check if room has active reservations
     * @param roomId Room ID
     * @return true if room has active reservations, false otherwise
     */
    public boolean hasActiveReservations(int roomId) {
        return roomDAO.hasActiveReservations(roomId);
    }

    /**
     * Get total number of rooms
     * @return Total count of rooms
     */
    public int getTotalRoomsCount() {
        return roomDAO.getTotalRoomsCount();
    }

    /**
     * Get count of available rooms
     * @return Count of available rooms
     */
    public int getAvailableRoomsCount() {
        return roomDAO.getAvailableRoomsCount();
    }

    /**
     * Get count of occupied rooms
     * @return Count of occupied rooms
     */
    public int getOccupiedRoomsCount() {
        return roomDAO.getOccupiedRoomsCount();
    }

    /**
     * Get all distinct room types
     * @return List of room types
     */
    public List<String> getAllRoomTypes() {
        return roomDAO.getAllRoomTypes();
    }

    /**
     * Get average room price by type
     * @param type Room type
     * @return Average price for the room type
     */
    public double getAverageRoomPriceByType(String type) {
        return roomDAO.getAverageRoomPriceByType(type);
    }

    /**
     * Get cheapest available room
     * @return Room with lowest price that's available
     */
    public Room getCheapestAvailableRoom() {
        return roomDAO.getCheapestAvailableRoom();
    }

    /**
     * Get most expensive available room
     * @return Room with highest price that's available
     */
    public Room getMostExpensiveAvailableRoom() {
        return roomDAO.getMostExpensiveAvailableRoom();
    }
}