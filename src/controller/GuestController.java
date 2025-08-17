package controller;

import dao.GuestDAO;
import model.Guest;
import java.util.List;

public class GuestController {
    private GuestDAO guestDAO;

    public GuestController() {
        this.guestDAO = new GuestDAO();
    }

    /**
     * Create a new guest
     * @param guest Guest object to create
     * @return true if successful, false otherwise
     */
    public boolean createGuest(Guest guest) {
        return guestDAO.createGuest(guest);
    }

    /**
     * Get all guests
     * @return List of all guests
     */
    public List<Guest> getAllGuests() {
        return guestDAO.getAllGuests();
    }

    /**
     * Get guest by ID
     * @param id Guest ID
     * @return Guest object or null if not found
     */
    public Guest getGuestById(int id) {
        return guestDAO.getGuestById(id);
    }

    /**
     * Search guests by name
     * @param name Name to search for (partial match)
     * @return List of matching guests
     */
    public List<Guest> searchGuestsByName(String name) {
        return guestDAO.searchGuestsByName(name);
    }

    /**
     * Search guest by contact
     * @param contact Contact to search for
     * @return Guest object or null if not found
     */
    public Guest getGuestByContact(String contact) {
        return guestDAO.getGuestByContact(contact);
    }

    /**
     * Update guest information
     * @param guest Guest object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateGuest(Guest guest) {
        return guestDAO.updateGuest(guest);
    }

    /**
     * Delete guest by ID
     * @param id Guest ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteGuest(int id) {
        return guestDAO.deleteGuest(id);
    }

    /**
     * Check if guest exists
     * @param id Guest ID
     * @return true if guest exists, false otherwise
     */
    public boolean guestExists(int id) {
        return guestDAO.guestExists(id);
    }

    /**
     * Check if guest has active reservations
     * @param guestId Guest ID
     * @return true if guest has active reservations, false otherwise
     */
    public boolean hasActiveReservations(int guestId) {
        return guestDAO.hasActiveReservations(guestId);
    }

    /**
     * Get total number of guests
     * @return Total count of guests
     */
    public int getTotalGuestsCount() {
        return guestDAO.getTotalGuestsCount();
    }

    /**
     * Get total number of VIP guests
     * @return Total count of VIP guests
     */
    public int getVipGuestsCount() {
        return guestDAO.getVipGuestsCount();
    }

    /**
     * Get all VIP guests
     * @return List of VIP guests
     */
    public List<Guest> getVipGuests() {
        return guestDAO.getVipGuests();
    }

    /**
     * Get all regular guests
     * @return List of regular guests
     */
    public List<Guest> getRegularGuests() {
        return guestDAO.getRegularGuests();
    }

    /**
     * Check if contact number is already registered
     * @param contact Contact number to check
     * @param excludeId Guest ID to exclude from check (for updates)
     * @return true if contact exists, false otherwise
     */
    public boolean isContactRegistered(String contact, int excludeId) {
        return guestDAO.isContactRegistered(contact, excludeId);
    }

    /**
     * Overloaded method for new guest registration
     * @param contact Contact number to check
     * @return true if contact exists, false otherwise
     */
    public boolean isContactRegistered(String contact) {
        return guestDAO.isContactRegistered(contact);
    }

    /**
     * Validate guest data before creating or updating
     * @param guest Guest object to validate
     * @return true if valid, false otherwise
     */
    public boolean validateGuest(Guest guest) {
        if (guest == null) {
            return false;
        }

        // Check if name is not null or empty
        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            return false;
        }

        // Check if contact is not null or empty
        if (guest.getContact() == null || guest.getContact().trim().isEmpty()) {
            return false;
        }

        // Basic contact format validation (you can enhance this as needed)
        String contact = guest.getContact().trim();
        if (contact.length() < 10) { // Assuming minimum 10 digits
            return false;
        }

        return true;
    }

    /**
     * Create guest with validation
     * @param guest Guest object to create
     * @return true if successful, false otherwise
     */
    public boolean createGuestWithValidation(Guest guest) {
        if (!validateGuest(guest)) {
            System.err.println("Guest validation failed");
            return false;
        }

        if (isContactRegistered(guest.getContact())) {
            System.err.println("Contact number already registered");
            return false;
        }

        return createGuest(guest);
    }

    /**
     * Update guest with validation
     * @param guest Guest object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateGuestWithValidation(Guest guest) {
        if (!validateGuest(guest)) {
            System.err.println("Guest validation failed");
            return false;
        }

        if (!guestExists(guest.getId())) {
            System.err.println("Guest does not exist");
            return false;
        }

        if (isContactRegistered(guest.getContact(), guest.getId())) {
            System.err.println("Contact number already registered by another guest");
            return false;
        }

        return updateGuest(guest);
    }

    /**
     * Get guest statistics
     * @return String containing guest statistics
     */
    public String getGuestStatistics() {
        int totalGuests = getTotalGuestsCount();
        int vipGuests = getVipGuestsCount();
        int regularGuests = totalGuests - vipGuests;

        StringBuilder stats = new StringBuilder();
        stats.append("Guest Statistics:\n");
        stats.append("Total Guests: ").append(totalGuests).append("\n");
        stats.append("VIP Guests: ").append(vipGuests).append("\n");
        stats.append("Regular Guests: ").append(regularGuests).append("\n");

        if (totalGuests > 0) {
            double vipPercentage = (double) vipGuests / totalGuests * 100;
            stats.append("VIP Percentage: ").append(String.format("%.1f", vipPercentage)).append("%\n");
        }

        return stats.toString();
    }
}