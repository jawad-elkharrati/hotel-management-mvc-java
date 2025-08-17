package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Guest;
import model.GuestType;
import model.GuestFactory;
import model.VipGuest;
import model.RegularGuest;

public class GuestManagementController {
    private GuestController guestController = new GuestController();
    private ObservableList<Guest> guestList = FXCollections.observableArrayList();

    public ObservableList<Guest> getGuestList() {
        return guestList;
    }

    public boolean addGuest(String name, String contact, GuestType selectedType) {
        if (!validateInput(name, contact)) {
            return false;
        }

        if (selectedType == null) {
            showAlert("Validation Error", "Please select a guest type!", Alert.AlertType.WARNING);
            return false;
        }

        // Check if contact is already registered
        if (guestController.isContactRegistered(contact)) {
            showAlert("Validation Error", "Contact number is already registered!", Alert.AlertType.WARNING);
            return false;
        }

        // Create appropriate guest type
        Guest guest = GuestFactory.createGuest(selectedType, name, contact, 0.25);

        if (guestController.createGuest(guest)) {
            showAlert("Success", "Guest added successfully!", Alert.AlertType.INFORMATION);
            refreshGuestList();
            return true;
        } else {
            showAlert("Error", "Failed to add guest. Please try again.", Alert.AlertType.ERROR);
            return false;
        }
    }

    public boolean updateGuest(Guest selected, String name, String contact, GuestType selectedType) {
        if (selected == null) {
            showAlert("Selection Error", "Please select a guest to update!", Alert.AlertType.WARNING);
            return false;
        }

        if (!validateInput(name, contact)) {
            return false;
        }

        if (selectedType == null) {
            showAlert("Validation Error", "Please select a guest type!", Alert.AlertType.WARNING);
            return false;
        }

        // Check if contact is already registered by another guest
        if (guestController.isContactRegistered(contact, selected.getId())) {
            showAlert("Validation Error", "Contact number is already registered by another guest!", Alert.AlertType.WARNING);
            return false;
        }

        // Check if guest type has changed
        boolean typeChanged = false;
        if ((selected instanceof VipGuest && selectedType == GuestType.REGULAR) ||
                (selected instanceof RegularGuest && selectedType == GuestType.VIP)) {
            typeChanged = true;
        }

        if (typeChanged) {
            // If type changed, we need to create a new guest object and update it
            Guest newGuest = GuestFactory.createGuest(selectedType, name, contact, 0.25);
            newGuest.setId(selected.getId());

            if (guestController.updateGuest(newGuest)) {
                showAlert("Success", "Guest updated successfully!", Alert.AlertType.INFORMATION);
                refreshGuestList();
                return true;
            } else {
                showAlert("Error", "Failed to update guest. Please try again.", Alert.AlertType.ERROR);
                return false;
            }
        } else {
            // Just update name and contact
            selected.setName(name);
            selected.setContact(contact);

            if (guestController.updateGuest(selected)) {
                showAlert("Success", "Guest updated successfully!", Alert.AlertType.INFORMATION);
                refreshGuestList();
                return true;
            } else {
                showAlert("Error", "Failed to update guest. Please try again.", Alert.AlertType.ERROR);
                return false;
            }
        }
    }

    public boolean deleteGuest(Guest selected) {
        if (selected == null) {
            showAlert("Selection Error", "Please select a guest to delete!", Alert.AlertType.WARNING);
            return false;
        }

        if (guestController.deleteGuest(selected.getId())) {
            showAlert("Success", "Guest deleted successfully!", Alert.AlertType.INFORMATION);
            refreshGuestList();
            return true;
        } else {
            showAlert("Error", "Failed to delete guest. Guest may have active reservations.", Alert.AlertType.ERROR);
            return false;
        }
    }

    public void searchGuests(String searchTerm) {
        if (searchTerm.isEmpty()) {
            refreshGuestList();
            return;
        }

        guestList.clear();
        guestList.addAll(guestController.searchGuestsByName(searchTerm));
    }

    public void refreshGuestList() {
        guestList.clear();
        guestList.addAll(guestController.getAllGuests());
    }

    public int getTotalGuestsCount() {
        return guestController.getTotalGuestsCount();
    }

    public int getVipGuestsCount() {
        return guestController.getVipGuestsCount();
    }

    public String getGuestTypeFromGuest(Guest guest) {
        if (guest instanceof VipGuest) {
            return "VIP";
        } else {
            return "REGULAR";
        }
    }

    public GuestType getGuestTypeEnumFromGuest(Guest guest) {
        if (guest instanceof VipGuest) {
            return GuestType.VIP;
        } else {
            return GuestType.REGULAR;
        }
    }

    private boolean validateInput(String name, String contact) {
        if (name == null || name.trim().isEmpty()) {
            showAlert("Validation Error", "Guest name is required!", Alert.AlertType.WARNING);
            return false;
        }

        if (contact == null || contact.trim().isEmpty()) {
            showAlert("Validation Error", "Contact information is required!", Alert.AlertType.WARNING);
            return false;
        }

        // Basic contact validation (you can enhance this)
        if (contact.trim().length() < 3) {
            showAlert("Validation Error", "Contact information must be at least 3 characters!", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}