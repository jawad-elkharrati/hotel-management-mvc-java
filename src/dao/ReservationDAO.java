package dao;

import model.*;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReservationDAO {

    /**
     * Create a new reservation
     * @param reservation Reservation object to create
     * @return true if successful, false otherwise
     */
    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (guest_id, room_id, checkin_date, checkout_date, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setDate(3, Date.valueOf(reservation.getCheckinDate()));
            stmt.setDate(4, Date.valueOf(reservation.getCheckoutDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(reservation.getCreatedAt()));

            boolean success = stmt.executeUpdate() > 0;

            // Set the generated ID back to the reservation object
            if (success) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setId(generatedKeys.getInt(1));
                    }
                }

                // Update room status to occupied
                updateRoomStatus(reservation.getRoomId(), "Occupied");
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all reservations with guest and room details
     * @return List of all reservations
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "ORDER BY r.created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Get reservation by ID
     * @param id Reservation ID
     * @return Reservation object or null if not found
     */
    public Reservation getReservationById(int id) {
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createReservationFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding reservation by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get reservations by guest ID
     * @param guestId Guest ID
     * @return List of reservations for the specified guest
     */
    public List<Reservation> getReservationsByGuest(int guestId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.guest_id = ? " +
                "ORDER BY r.checkin_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations by guest: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Get active reservations (currently checked in)
     * @return List of active reservations
     */
    public List<Reservation> getActiveReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE CURRENT_DATE >= r.checkin_date AND CURRENT_DATE < r.checkout_date " +
                "ORDER BY r.checkin_date";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving active reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Get upcoming reservations (check-in date is in the future)
     * @return List of upcoming reservations
     */
    public List<Reservation> getUpcomingReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.checkin_date > CURRENT_DATE " +
                "ORDER BY r.checkin_date";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving upcoming reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Get reservations by room ID
     * @param roomId Room ID
     * @return List of reservations for the specified room
     */
    public List<Reservation> getReservationsByRoom(int roomId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE r.room_id = ? " +
                "ORDER BY r.checkin_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations by room: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Get reservations for a specific date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of reservations within the date range
     */
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, g.name as guest_name, g.contact as guest_contact, " +
                "rm.number as room_number, rm.type as room_type, rm.status as room_status " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.id " +
                "JOIN rooms rm ON r.room_id = rm.id " +
                "WHERE (r.checkin_date >= ? AND r.checkin_date <= ?) " +
                "OR (r.checkout_date >= ? AND r.checkout_date <= ?) " +
                "OR (r.checkin_date < ? AND r.checkout_date > ?) " +
                "ORDER BY r.checkin_date";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));
            stmt.setDate(5, Date.valueOf(startDate));
            stmt.setDate(6, Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = createReservationFromResultSet(rs);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reservations by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Update reservation information
     * @param reservation Reservation object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE reservations SET guest_id=?, room_id=?, checkin_date=?, checkout_date=? WHERE id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setDate(3, Date.valueOf(reservation.getCheckinDate()));
            stmt.setDate(4, Date.valueOf(reservation.getCheckoutDate()));
            stmt.setInt(5, reservation.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete reservation by ID
     * @param reservationId Reservation ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteReservation(int reservationId) {
        // First get the reservation to find the room ID
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }

        String sql = "DELETE FROM reservations WHERE id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            boolean success = stmt.executeUpdate() > 0;

            // Update room status back to available
            if (success) {
                updateRoomStatus(reservation.getRoomId(), "Available");
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if room is available for a date range
     * @param roomId Room ID
     * @param checkinDate Check-in date
     * @param checkoutDate Check-out date
     * @return true if room is available, false otherwise
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND " +
                "(checkin_date < ? AND checkout_date > ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkoutDate));
            stmt.setDate(3, Date.valueOf(checkinDate));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking room availability: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if room is available for a date range excluding a specific reservation
     * @param roomId Room ID
     * @param checkinDate Check-in date
     * @param checkoutDate Check-out date
     * @param excludeReservationId Reservation ID to exclude from check
     * @return true if room is available, false otherwise
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkinDate, LocalDate checkoutDate, int excludeReservationId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND id != ? AND " +
                "(checkin_date < ? AND checkout_date > ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setInt(2, excludeReservationId);
            stmt.setDate(3, Date.valueOf(checkoutDate));
            stmt.setDate(4, Date.valueOf(checkinDate));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking room availability: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if reservation exists
     * @param id Reservation ID
     * @return true if reservation exists, false otherwise
     */
    public boolean reservationExists(int id) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if reservation exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total number of reservations
     * @return Total count of reservations
     */
    public int getTotalReservationsCount() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total reservations count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get count of active reservations
     * @return Count of active reservations
     */
    public int getActiveReservationsCount() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE CURRENT_DATE >= checkin_date AND CURRENT_DATE < checkout_date";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active reservations count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get count of upcoming reservations
     * @return Count of upcoming reservations
     */
    public int getUpcomingReservationsCount() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE checkin_date > CURRENT_DATE";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting upcoming reservations count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Check out a guest (update room status to available)
     * @param reservationId Reservation ID
     * @return true if successful, false otherwise
     */
    public boolean checkOutGuest(int reservationId) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }
        return updateRoomStatus(reservation.getRoomId(), "Available");
    }

    /**
     * Check in a guest (update room status to occupied)
     * @param reservationId Reservation ID
     * @return true if successful, false otherwise
     */
    public boolean checkInGuest(int reservationId) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }
        return updateRoomStatus(reservation.getRoomId(), "Occupied");
    }

    /**
     * Update room status
     * @param roomId Room ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    private boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE id=?";
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
     * Helper method to create Reservation object from ResultSet
     * @param rs ResultSet containing reservation data
     * @return Reservation object
     * @throws SQLException if error occurs while reading ResultSet
     */
    private Reservation createReservationFromResultSet(ResultSet rs) throws SQLException {
        // Get guest type from database (assumes you have a guest_type column)
        String guestTypeStr = rs.getString("guest_type");
        GuestType guestType = GuestType.valueOf(guestTypeStr.toUpperCase());

        // Create Guest object using factory
        Guest guest = GuestFactory.createGuest(guestType,
                rs.getString("guest_name"),
                rs.getString("guest_contact"),rs.getDouble("discount_rate"));
        guest.setId(rs.getInt("guest_id"));

        // If it's a VIP guest, set the discount rate
        if (guest instanceof VipGuest) {
            double discountRate = rs.getDouble("discount_rate"); // assumes discount_rate column exists

        }

        // Create Room object with full data
        Room room = new Room();
        room.setId(rs.getInt("room_id"));
        room.setNumber(rs.getString("room_number"));
        room.setType(rs.getString("room_type"));
        // Note: Fixed method name - assuming you meant setStatus instead of setStatus
        // room.setStatus(rs.getString("room_status")); // Uncomment if Room has status field

        // Create Reservation with proper relationships
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setCheckinDate(rs.getDate("checkin_date").toLocalDate());
        reservation.setCheckoutDate(rs.getDate("checkout_date").toLocalDate());

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            reservation.setCreatedAt(createdAt.toLocalDateTime());
        }

        return reservation;
    }}
