package dao;

import model.Room;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class RoomDAO {

    /**
     * Create a new room
     * @param room Room object to create
     * @return true if successful, false otherwise
     */
    public boolean createRoom(Room room) {
        String sql = "INSERT INTO rooms (number, type, status, base_price, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setString(3, room.getStatus());
            stmt.setDouble(4, room.getBasePrice());
            stmt.setString(5, room.getDescription());

            boolean success = stmt.executeUpdate() > 0;

            // Set the generated ID back to the room object
            if (success) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        room.setId(generatedKeys.getInt(1));
                    }
                }
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error creating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all rooms
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get room by ID
     * @param id Room ID
     * @return Room object or null if not found
     */
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding room by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get room by room number
     * @param number Room number
     * @return Room object or null if not found
     */
    public Room getRoomByNumber(String number) {
        String sql = "SELECT * FROM rooms WHERE number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding room by number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get available rooms
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available' ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get rooms by type
     * @param type Room type
     * @return List of rooms of specified type
     */
    public List<Room> getRoomsByType(String type) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE type = ? ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rooms by type: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get available rooms by type
     * @param type Room type
     * @return List of available rooms of specified type
     */
    public List<Room> getAvailableRoomsByType(String type) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE type = ? AND status = 'Available' ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available rooms by type: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get rooms available for specific date range
     * @param checkinDate Check-in date
     * @param checkoutDate Check-out date
     * @return List of available rooms for the date range
     */
    public List<Room> getAvailableRoomsForDateRange(LocalDate checkinDate, LocalDate checkoutDate) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.* FROM rooms r WHERE r.status = 'Available' AND r.id NOT IN " +
                "(SELECT DISTINCT room_id FROM reservations WHERE " +
                "(checkin_date < ? AND checkout_date > ?)) ORDER BY r.number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(checkoutDate));
            stmt.setDate(2, Date.valueOf(checkinDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available rooms for date range: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get rooms within a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of rooms within the price range
     */
    public List<Room> getRoomsByPriceRange(double minPrice, double maxPrice) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE base_price >= ? AND base_price <= ? ORDER BY base_price, number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rooms by price range: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Get available rooms within a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of available rooms within the price range
     */
    public List<Room> getAvailableRoomsByPriceRange(double minPrice, double maxPrice) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available' AND base_price >= ? AND base_price <= ? ORDER BY base_price, number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available rooms by price range: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Search rooms by description keyword
     * @param keyword Keyword to search in description
     * @return List of rooms matching the keyword
     */
    public List<Room> searchRoomsByDescription(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE description LIKE ? ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error searching rooms by description: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Search available rooms by description keyword
     * @param keyword Keyword to search in description
     * @return List of available rooms matching the keyword
     */
    public List<Room> searchAvailableRoomsByDescription(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available' AND description LIKE ? ORDER BY number";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = createRoomFromResultSet(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error searching available rooms by description: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Update room information
     * @param room Room object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET number = ?, type = ?, status = ?, base_price = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setString(3, room.getStatus());
            stmt.setDouble(4, room.getBasePrice());
            stmt.setString(5, room.getDescription());
            stmt.setInt(6, room.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update room status
     * @param roomId Room ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, roomId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update room price
     * @param roomId Room ID
     * @param newPrice New base price
     * @return true if successful, false otherwise
     */
    public boolean updateRoomPrice(int roomId, double newPrice) {
        String sql = "UPDATE rooms SET base_price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, roomId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room price: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update room description
     * @param roomId Room ID
     * @param description New description
     * @return true if successful, false otherwise
     */
    public boolean updateRoomDescription(int roomId, String description) {
        String sql = "UPDATE rooms SET description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, description);
            stmt.setInt(2, roomId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room description: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete room by ID
     * @param id Room ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRoom(int id) {
        // First check if room has any active reservations
        if (hasActiveReservations(id)) {
            System.err.println("Cannot delete room with active reservations");
            return false;
        }

        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if room exists
     * @param id Room ID
     * @return true if room exists, false otherwise
     */
    public boolean roomExists(int id) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if room exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if room number is already taken
     * @param number Room number to check
     * @param excludeId Room ID to exclude from check (for updates)
     * @return true if room number exists, false otherwise
     */
    public boolean isRoomNumberTaken(String number, int excludeId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE number = ? AND id != ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, number);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking room number: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Overloaded method for new room creation
     * @param number Room number to check
     * @return true if room number exists, false otherwise
     */
    public boolean isRoomNumberTaken(String number) {
        return isRoomNumberTaken(number, -1);
    }

    /**
     * Check if room has active reservations
     * @param roomId Room ID
     * @return true if room has active reservations, false otherwise
     */
    public boolean hasActiveReservations(int roomId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND checkout_date >= CURRENT_DATE";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking active reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total number of rooms
     * @return Total count of rooms
     */
    public int getTotalRoomsCount() {
        String sql = "SELECT COUNT(*) FROM rooms";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total rooms count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get count of available rooms
     * @return Count of available rooms
     */
    public int getAvailableRoomsCount() {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status = 'Available'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available rooms count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get count of occupied rooms
     * @return Count of occupied rooms
     */
    public int getOccupiedRoomsCount() {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status = 'Occupied'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting occupied rooms count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get all distinct room types
     * @return List of room types
     */
    public List<String> getAllRoomTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM rooms ORDER BY type";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving room types: " + e.getMessage());
            e.printStackTrace();
        }
        return types;
    }

    /**
     * Get average room price by type
     * @param type Room type
     * @return Average price for the room type
     */
    public double getAverageRoomPriceByType(String type) {
        String sql = "SELECT AVG(base_price) FROM rooms WHERE type = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting average room price by type: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Get cheapest available room
     * @return Room with lowest price that's available
     */
    public Room getCheapestAvailableRoom() {
        String sql = "SELECT * FROM rooms WHERE status = 'Available' ORDER BY base_price ASC LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return createRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cheapest available room: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get most expensive available room
     * @return Room with highest price that's available
     */
    public Room getMostExpensiveAvailableRoom() {
        String sql = "SELECT * FROM rooms WHERE status = 'Available' ORDER BY base_price DESC LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return createRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting most expensive available room: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to create Room object from ResultSet
     * @param rs ResultSet containing room data
     * @return Room object
     * @throws SQLException if error occurs while reading ResultSet
     */
    private Room createRoomFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setNumber(rs.getString("number"));
        room.setType(rs.getString("type"));
        room.setStatus(rs.getString("status"));
        room.setBasePrice(rs.getDouble("base_price"));
        room.setDescription(rs.getString("description"));
        return room;
    }
}