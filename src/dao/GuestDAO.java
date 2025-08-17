package dao;

import model.*;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    /**
     * Create a new guest
     * @param guest Guest object to create
     * @return true if successful, false otherwise
     */
    public boolean createGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, contact, guest_type, discount_rate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getContact());
            stmt.setString(3, guest.getGuestType());

            // Set discount rate for VIP guests
            if (guest instanceof VipGuest) {
                stmt.setDouble(4, ((VipGuest) guest).getDiscountRate());
            } else {
                stmt.setDouble(4, 0.0);
            }

            boolean success = stmt.executeUpdate() > 0;

            // Set the generated ID back to the guest object
            if (success) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        guest.setId(generatedKeys.getInt(1));
                    }
                }
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error creating guest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all guests
     * @return List of all guests
     */
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY name";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = createGuestFromResultSet(rs);
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving guests: " + e.getMessage());
            e.printStackTrace();
        }
        return guests;
    }

    /**
     * Get guest by ID
     * @param id Guest ID
     * @return Guest object or null if not found
     */
    public Guest getGuestById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createGuestFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding guest by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Search guests by name
     * @param name Name to search for (partial match)
     * @return List of matching guests
     */
    public List<Guest> searchGuestsByName(String name) {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests WHERE name LIKE ? ORDER BY name";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Guest guest = createGuestFromResultSet(rs);
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Error searching guests by name: " + e.getMessage());
            e.printStackTrace();
        }
        return guests;
    }

    /**
     * Search guest by contact
     * @param contact Contact to search for
     * @return Guest object or null if not found
     */
    public Guest getGuestByContact(String contact) {
        String sql = "SELECT * FROM guests WHERE contact = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createGuestFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding guest by contact: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update guest information
     * @param guest Guest object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE guests SET name = ?, contact = ?, guest_type = ?, discount_rate = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getContact());
            stmt.setString(3, guest.getGuestType());

            if (guest instanceof VipGuest) {
                stmt.setDouble(4, ((VipGuest) guest).getDiscountRate());
            } else {
                stmt.setDouble(4, 0.0);
            }
            stmt.setInt(5, guest.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating guest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete guest by ID
     * @param id Guest ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteGuest(int id) {
        // First check if guest has any active reservations
        if (hasActiveReservations(id)) {
            System.err.println("Cannot delete guest with active reservations");
            return false;
        }

        String sql = "DELETE FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting guest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if guest exists
     * @param id Guest ID
     * @return true if guest exists, false otherwise
     */
    public boolean guestExists(int id) {
        String sql = "SELECT COUNT(*) FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if guest exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if guest has active reservations
     * @param guestId Guest ID
     * @return true if guest has active reservations, false otherwise
     */
    public boolean hasActiveReservations(int guestId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE guest_id = ? AND checkout_date >= CURRENT_DATE";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
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
     * Get total number of guests
     * @return Total count of guests
     */
    public int getTotalGuestsCount() {
        String sql = "SELECT COUNT(*) FROM guests";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total guests count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total number of VIP guests
     * @return Total count of VIP guests
     */
    public int getVipGuestsCount() {
        String sql = "SELECT COUNT(*) FROM guests WHERE guest_type = 'VIP'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting VIP guests count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get all VIP guests
     * @return List of VIP guests
     */
    public List<Guest> getVipGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests WHERE guest_type = 'VIP' ORDER BY name";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = createGuestFromResultSet(rs);
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving VIP guests: " + e.getMessage());
            e.printStackTrace();
        }
        return guests;
    }

    /**
     * Get all regular guests
     * @return List of regular guests
     */
    public List<Guest> getRegularGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests WHERE guest_type = 'REGULAR' ORDER BY name";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = createGuestFromResultSet(rs);
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving regular guests: " + e.getMessage());
            e.printStackTrace();
        }
        return guests;
    }

    /**
     * Check if contact number is already registered
     * @param contact Contact number to check
     * @param excludeId Guest ID to exclude from check (for updates)
     * @return true if contact exists, false otherwise
     */
    public boolean isContactRegistered(String contact, int excludeId) {
        String sql = "SELECT COUNT(*) FROM guests WHERE contact = ? AND id != ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking contact registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Overloaded method for new guest registration
     * @param contact Contact number to check
     * @return true if contact exists, false otherwise
     */
    public boolean isContactRegistered(String contact) {
        return isContactRegistered(contact, -1);
    }

    /**
     * Helper method to create Guest object from ResultSet using Factory Pattern
     * @param rs ResultSet containing guest data
     * @return Guest object (VipGuest or RegularGuest)
     * @throws SQLException if error occurs while reading ResultSet
     */
    private Guest createGuestFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String contact = rs.getString("contact");
        String guestType = rs.getString("guest_type");
        double discountRate = rs.getDouble("discount_rate");

        Guest guest;

        // Handle potential null values from database
        if (guestType == null || guestType.isEmpty()) {
            guestType = "REGULAR"; // Default to regular if null
        }

        guest=GuestFactory.createGuest(guestType,name,contact,discountRate);

        guest.setId(id);
        return guest;
    }
}