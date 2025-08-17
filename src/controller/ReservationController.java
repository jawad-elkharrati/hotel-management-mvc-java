package controller;

import dao.ReservationDAO;
import model.Reservation;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {
    private ReservationDAO reservationDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }

    /**
     * Create a new reservation
     * @param reservation Reservation object to create
     * @return true if successful, false otherwise
     */
    public boolean createReservation(Reservation reservation) {
        return reservationDAO.createReservation(reservation);
    }

    /**
     * Get all reservations
     * @return List of all reservations with guest and room details
     */
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    /**
     * Get reservation by ID
     * @param id Reservation ID
     * @return Reservation object with guest and room details or null if not found
     */
    public Reservation getReservationById(int id) {
        return reservationDAO.getReservationById(id);
    }

    /**
     * Get reservations by guest ID
     * @param guestId Guest ID
     * @return List of reservations for the specified guest
     */
    public List<Reservation> getReservationsByGuest(int guestId) {
        return reservationDAO.getReservationsByGuest(guestId);
    }

    /**
     * Get active reservations (currently checked in)
     * @return List of active reservations
     */
    public List<Reservation> getActiveReservations() {
        return reservationDAO.getActiveReservations();
    }

    /**
     * Get upcoming reservations (check-in date is in the future)
     * @return List of upcoming reservations
     */
    public List<Reservation> getUpcomingReservations() {
        return reservationDAO.getUpcomingReservations();
    }

    /**
     * Get reservations by room ID
     * @param roomId Room ID
     * @return List of reservations for the specified room
     */
    public List<Reservation> getReservationsByRoom(int roomId) {
        return reservationDAO.getReservationsByRoom(roomId);
    }

    /**
     * Get reservations for a specific date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of reservations within the date range
     */
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationDAO.getReservationsByDateRange(startDate, endDate);
    }

    /**
     * Update reservation information
     * @param reservation Reservation object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.updateReservation(reservation);
    }

    /**
     * Delete reservation by ID
     * @param reservationId Reservation ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteReservation(int reservationId) {
        return reservationDAO.deleteReservation(reservationId);
    }

    /**
     * Check if room is available for a date range
     * @param roomId Room ID
     * @param checkinDate Check-in date
     * @param checkoutDate Check-out date
     * @return true if room is available, false otherwise
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        return reservationDAO.isRoomAvailable(roomId, checkinDate, checkoutDate);
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
        return reservationDAO.isRoomAvailable(roomId, checkinDate, checkoutDate, excludeReservationId);
    }

    /**
     * Check if reservation exists
     * @param id Reservation ID
     * @return true if reservation exists, false otherwise
     */
    public boolean reservationExists(int id) {
        return reservationDAO.reservationExists(id);
    }

    /**
     * Get total number of reservations
     * @return Total count of reservations
     */
    public int getTotalReservationsCount() {
        return reservationDAO.getTotalReservationsCount();
    }

    /**
     * Get count of active reservations
     * @return Count of active reservations
     */
    public int getActiveReservationsCount() {
        return reservationDAO.getActiveReservationsCount();
    }

    /**
     * Get count of upcoming reservations
     * @return Count of upcoming reservations
     */
    public int getUpcomingReservationsCount() {
        return reservationDAO.getUpcomingReservationsCount();
    }

    /**
     * Check out a guest (update room status to available)
     * @param reservationId Reservation ID
     * @return true if successful, false otherwise
     */
    public boolean checkOutGuest(int reservationId) {
        return reservationDAO.checkOutGuest(reservationId);
    }

    /**
     * Check in a guest (update room status to occupied)
     * @param reservationId Reservation ID
     * @return true if successful, false otherwise
     */
    public boolean checkInGuest(int reservationId) {
        return reservationDAO.checkInGuest(reservationId);
    }
}