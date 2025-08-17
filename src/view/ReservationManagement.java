package view;

import controller.ReservationManagementController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Guest;
import model.Reservation;
import model.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationManagement extends Application {
    private ReservationManagementController controller = new ReservationManagementController();

    private TableView<Reservation> table = new TableView<>();
    private ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

    // Form controls
    private ComboBox<Guest> guestCombo = new ComboBox<>();
    private ComboBox<Room> roomCombo = new ComboBox<>();
    private DatePicker checkinDate = new DatePicker();
    private DatePicker checkoutDate = new DatePicker();
    private TextField guestSearchField = new TextField();
    private ComboBox<String> filterCombo = new ComboBox<>();

    // Labels for room availability feedback
    private Label roomStatusLabel = new Label();
    private Label reservationCountLabel = new Label();

    // Store button references for easy access
    private Button createBtn;
    private Button clearBtn;
    private Button updateBtn;
    private Button searchBtn;
    private Button refreshBtn;
    private Button deleteBtn;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hotel Reservation Management System");

        // Create main layout with compact styling
        VBox mainLayout = createMainLayout();

        // Setup components
        setupTable();
        setupFormControls();
        setupEventHandlers();

        // Initial data load
        refreshAllData();

        // Reduced window size for better fit
        Scene scene = new Scene(mainLayout, 900, 600);

        // Compact CSS styling
        scene.getStylesheets().add("data:text/css," + getCompactCSS());

        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(550);
        stage.show();
    }

    private String getCompactCSS() {
        return """
            .root {
                -fx-background-color: #f8fafc;
                -fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                -fx-font-size: 12px;
            }
            
            .main-title {
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                -fx-text-fill: #1e293b;
                -fx-padding: 0 0 10 0;
            }
            
            .section-card {
                -fx-background-color: white;
                -fx-background-radius: 8px;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);
                -fx-padding: 12px;
                -fx-spacing: 8px;
            }
            
            .section-title {
                -fx-font-size: 14px;
                -fx-font-weight: 600;
                -fx-text-fill: #374151;
            }
            
            .form-label {
                -fx-font-size: 11px;
                -fx-font-weight: 500;
                -fx-text-fill: #4b5563;
                -fx-min-width: 70px;
            }
            
            .combo-box, .date-picker, .text-field {
                -fx-background-color: white;
                -fx-border-color: #d1d5db;
                -fx-border-width: 1px;
                -fx-border-radius: 6px;
                -fx-background-radius: 6px;
                -fx-padding: 4px 8px;
                -fx-font-size: 11px;
            }
            
            .combo-box:focused, .date-picker:focused, .text-field:focused {
                -fx-border-color: #3b82f6;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.25), 3, 0, 0, 0);
            }
            
            .btn-primary {
                -fx-background-color: linear-gradient(to bottom, #3b82f6, #2563eb);
                -fx-text-fill: white;
                -fx-background-radius: 6px;
                -fx-padding: 6px 12px;
                -fx-font-size: 11px;
                -fx-font-weight: 500;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.25), 3, 0, 0, 1);
            }
            
            .btn-primary:hover {
                -fx-background-color: linear-gradient(to bottom, #2563eb, #1d4ed8);
            }
            
            .btn-success {
                -fx-background-color: linear-gradient(to bottom, #10b981, #059669);
                -fx-text-fill: white;
                -fx-background-radius: 6px;
                -fx-padding: 6px 12px;
                -fx-font-size: 11px;
                -fx-font-weight: 500;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.25), 3, 0, 0, 1);
            }
            
            .btn-success:hover {
                -fx-background-color: linear-gradient(to bottom, #059669, #047857);
            }
            
            .btn-danger {
                -fx-background-color: linear-gradient(to bottom, #ef4444, #dc2626);
                -fx-text-fill: white;
                -fx-background-radius: 6px;
                -fx-padding: 6px 12px;
                -fx-font-size: 11px;
                -fx-font-weight: 500;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.25), 3, 0, 0, 1);
            }
            
            .btn-danger:hover {
                -fx-background-color: linear-gradient(to bottom, #dc2626, #b91c1c);
            }
            
            .btn-secondary {
                -fx-background-color: white;
                -fx-text-fill: #6b7280;
                -fx-border-color: #d1d5db;
                -fx-border-width: 1px;
                -fx-background-radius: 6px;
                -fx-border-radius: 6px;
                -fx-padding: 6px 12px;
                -fx-font-size: 11px;
                -fx-font-weight: 500;
                -fx-cursor: hand;
            }
            
            .btn-secondary:hover {
                -fx-background-color: #f9fafb;
                -fx-border-color: #9ca3af;
            }
            
            .table-view {
                -fx-background-color: white;
                -fx-table-cell-border-color: #f3f4f6;
                -fx-background-radius: 6px;
                -fx-font-size: 11px;
            }
            
            .table-view .column-header {
                -fx-background-color: #f8fafc;
                -fx-font-weight: 600;
                -fx-text-fill: #374151;
                -fx-font-size: 11px;
                -fx-padding: 6px 4px;
            }
            
            .table-view .table-row-cell {
                -fx-border-color: transparent;
                -fx-border-width: 0 0 1 0;
                -fx-table-cell-border-color: #f3f4f6;
                -fx-cell-size: 22px;
            }
            
            .table-view .table-row-cell:selected {
                -fx-background-color: #eff6ff;
                -fx-text-fill: #1e40af;
            }
            
            .status-available {
                -fx-text-fill: #059669;
                -fx-font-weight: 500;
            }
            
            .status-unavailable {
                -fx-text-fill: #dc2626;
                -fx-font-weight: 500;
            }
            
            .status-bar {
                -fx-background-color: #f8fafc;
                -fx-border-color: #e5e7eb;
                -fx-border-width: 1 0 0 0;
                -fx-padding: 8px 12px;
            }
            
            .status-text {
                -fx-font-size: 11px;
                -fx-text-fill: #6b7280;
            }
            
            .search-field {
                -fx-background-color: white;
                -fx-border-color: #d1d5db;
                -fx-border-width: 1px;
                -fx-border-radius: 6px;
                -fx-background-radius: 6px;
                -fx-padding: 4px 8px;
                -fx-font-size: 11px;
                -fx-prompt-text-fill: #9ca3af;
            }
            """;
    }

    private VBox createMainLayout() {
        VBox mainLayout = new VBox(12);
        mainLayout.setPadding(new Insets(16));
        mainLayout.getStyleClass().add("root");

        // Compact title
        Label titleLabel = new Label("🏨 Hotel Reservations");
        titleLabel.getStyleClass().add("main-title");

        // Create sections with compact design
        VBox formSection = createFormSection();
        VBox controlSection = createControlSection();
        VBox tableSection = createTableSection();
        HBox statusSection = createStatusSection();

        mainLayout.getChildren().addAll(titleLabel, formSection, controlSection, tableSection, statusSection);
        return mainLayout;
    }

    private VBox createFormSection() {
        VBox formSection = new VBox(8);
        formSection.getStyleClass().add("section-card");

        Label formTitle = new Label("✨ New Reservation");
        formTitle.getStyleClass().add("section-title");

        // Guest selection row - more compact
        HBox guestRow = new HBox(8);
        guestRow.setAlignment(Pos.CENTER_LEFT);
        Label guestLabel = new Label("Guest:");
        guestLabel.getStyleClass().add("form-label");
        guestCombo.setPrefWidth(180);
        guestCombo.setPromptText("Select guest");
        guestCombo.getStyleClass().add("combo-box");

        Button newGuestBtn = new Button("+ Guest");
        newGuestBtn.getStyleClass().add("btn-secondary");
        newGuestBtn.setOnAction(e -> openGuestManagement());
        guestRow.getChildren().addAll(guestLabel, guestCombo, newGuestBtn);

        // Room selection row
        HBox roomRow = new HBox(8);
        roomRow.setAlignment(Pos.CENTER_LEFT);
        Label roomLabel = new Label("Room:");
        roomLabel.getStyleClass().add("form-label");
        roomCombo.setPrefWidth(180);
        roomCombo.setPromptText("Select room");
        roomCombo.getStyleClass().add("combo-box");

        Button checkAvailBtn = new Button("Check");
        checkAvailBtn.getStyleClass().add("btn-secondary");
        checkAvailBtn.setOnAction(e -> checkRoomAvailability());

        roomStatusLabel.setPrefWidth(80);
        roomRow.getChildren().addAll(roomLabel, roomCombo, checkAvailBtn, roomStatusLabel);

        // Date selection row - more compact
        HBox dateRow = new HBox(8);
        dateRow.setAlignment(Pos.CENTER_LEFT);
        Label checkinLabel = new Label("Check-in:");
        checkinLabel.getStyleClass().add("form-label");
        checkinDate.setPrefWidth(120);
        checkinDate.getStyleClass().add("date-picker");

        Label checkoutLabel = new Label("Check-out:");
        checkoutLabel.getStyleClass().add("form-label");
        checkoutLabel.setPrefWidth(65);
        checkoutDate.setPrefWidth(120);
        checkoutDate.getStyleClass().add("date-picker");
        dateRow.getChildren().addAll(checkinLabel, checkinDate, checkoutLabel, checkoutDate);

        // Action buttons - more compact
        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        createBtn = new Button("✓ Create");
        createBtn.getStyleClass().add("btn-success");

        clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("btn-secondary");

        updateBtn = new Button("📝 Update");
        updateBtn.getStyleClass().add("btn-primary");

        buttonRow.getChildren().addAll(createBtn, clearBtn, updateBtn);

        formSection.getChildren().addAll(formTitle, guestRow, roomRow, dateRow, buttonRow);
        return formSection;
    }

    private VBox createControlSection() {
        VBox controlSection = new VBox(8);
        controlSection.getStyleClass().add("section-card");

        Label controlTitle = new Label("🔍 Search & Filter");
        controlTitle.getStyleClass().add("section-title");

        // Search and filter row - more compact
        HBox searchRow = new HBox(8);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("form-label");
        guestSearchField.setPrefWidth(140);
        guestSearchField.setPromptText("Guest name...");
        guestSearchField.getStyleClass().add("search-field");

        Label filterLabel = new Label("Filter:");
        filterLabel.getStyleClass().add("form-label");
        filterCombo.setPrefWidth(120);
        filterCombo.getItems().addAll("All", "Active", "Future", "Past");
        filterCombo.setValue("All");
        filterCombo.getStyleClass().add("combo-box");

        searchBtn = new Button("🔍");
        searchBtn.getStyleClass().add("btn-primary");

        refreshBtn = new Button("🔄");
        refreshBtn.getStyleClass().add("btn-secondary");

        deleteBtn = new Button("🗑️ Delete");
        deleteBtn.getStyleClass().add("btn-danger");

        searchRow.getChildren().addAll(searchLabel, guestSearchField, filterLabel, filterCombo,
                searchBtn, refreshBtn, deleteBtn);

        controlSection.getChildren().addAll(controlTitle, searchRow);
        return controlSection;
    }

    private VBox createTableSection() {
        VBox tableSection = new VBox(8);
        tableSection.getStyleClass().add("section-card");

        Label tableTitle = new Label("📋 Reservations");
        tableTitle.getStyleClass().add("section-title");

        table.setPrefHeight(280); // Reduced height
        table.getStyleClass().add("table-view");

        tableSection.getChildren().addAll(tableTitle, table);
        return tableSection;
    }

    private HBox createStatusSection() {
        HBox statusSection = new HBox(16);
        statusSection.getStyleClass().add("status-bar");
        statusSection.setAlignment(Pos.CENTER_LEFT);

        Label statusIcon = new Label("📊");

        reservationCountLabel.setText("Total: 0");
        reservationCountLabel.getStyleClass().add("status-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lastUpdated = new Label("Updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")));
        lastUpdated.getStyleClass().add("status-text");

        statusSection.getChildren().addAll(statusIcon, reservationCountLabel, spacer, lastUpdated);
        return statusSection;
    }

    private void setupTable() {
        // Guest Name Column - reduced width
        TableColumn<Reservation, String> guestCol = new TableColumn<>("Guest");
        guestCol.setCellValueFactory(data -> {
            Guest guest = data.getValue().getGuest();
            return new javafx.beans.property.SimpleStringProperty(guest != null ? guest.getName() : "N/A");
        });
        guestCol.setPrefWidth(100);

        // Guest Contact Column - reduced width
        TableColumn<Reservation, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(data -> {
            Guest guest = data.getValue().getGuest();
            return new javafx.beans.property.SimpleStringProperty(guest != null ? guest.getContact() : "N/A");
        });
        contactCol.setPrefWidth(100);

        // Room Number Column
        TableColumn<Reservation, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data -> {
            Room room = data.getValue().getRoom();
            return new javafx.beans.property.SimpleStringProperty(room != null ? room.getNumber() : "N/A");
        });
        roomCol.setPrefWidth(60);

        // Room Type Column
        TableColumn<Reservation, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> {
            Room room = data.getValue().getRoom();
            return new javafx.beans.property.SimpleStringProperty(room != null ? room.getType() : "N/A");
        });
        typeCol.setPrefWidth(80);

        // Check-in Date Column
        TableColumn<Reservation, LocalDate> checkinCol = new TableColumn<>("Check-in");
        checkinCol.setCellValueFactory(new PropertyValueFactory<>("checkinDate"));
        checkinCol.setPrefWidth(85);

        // Check-out Date Column
        TableColumn<Reservation, LocalDate> checkoutCol = new TableColumn<>("Check-out");
        checkoutCol.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        checkoutCol.setPrefWidth(85);

        // Status Column with styling
        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            LocalDate today = LocalDate.now();
            LocalDate checkin = data.getValue().getCheckinDate();
            LocalDate checkout = data.getValue().getCheckoutDate();

            String status;
            if (today.isBefore(checkin)) {
                status = "Future";
            } else if (today.isAfter(checkout)) {
                status = "Past";
            } else {
                status = "Active";
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        statusCol.setPrefWidth(60);

        // Created At Column - compact format
        TableColumn<Reservation, String> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(data -> {
            LocalDateTime created = data.getValue().getCreatedAt();
            String formatted = created != null ? created.format(DateTimeFormatter.ofPattern("MM/dd")) : "N/A";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });
        createdCol.setPrefWidth(60);

        table.getColumns().addAll(guestCol, contactCol, roomCol, typeCol, checkinCol, checkoutCol, statusCol, createdCol);
        table.setItems(reservationList);

        // Row selection handler
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFormFromSelection(newSelection);
            }
        });
    }

    private void setupFormControls() {
        // Date pickers - set minimum date to today
        checkinDate.setValue(LocalDate.now());
        checkoutDate.setValue(LocalDate.now().plusDays(1));

        // Date validation
        checkinDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && checkoutDate.getValue() != null && !newDate.isBefore(checkoutDate.getValue())) {
                checkoutDate.setValue(newDate.plusDays(1));
            }
            if (newDate != null && checkoutDate.getValue() != null) {
                checkRoomAvailability();
            }
        });

        checkoutDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && checkinDate.getValue() != null && !newDate.isAfter(checkinDate.getValue())) {
                checkinDate.setValue(newDate.minusDays(1));
            }
            if (checkinDate.getValue() != null && newDate != null) {
                checkRoomAvailability();
            }
        });

        // Room selection handler
        roomCombo.valueProperty().addListener((obs, oldRoom, newRoom) -> {
            if (newRoom != null && checkinDate.getValue() != null && checkoutDate.getValue() != null) {
                checkRoomAvailability();
            }
        });
    }

    private void setupEventHandlers() {
        // Create reservation button
        createBtn.setOnAction(e -> createReservation());

        // Clear form button
        clearBtn.setOnAction(e -> clearForm());

        // Update reservation button
        updateBtn.setOnAction(e -> updateSelectedReservation());

        // Delete reservation button
        deleteBtn.setOnAction(e -> deleteSelectedReservation());

        // Search button
        searchBtn.setOnAction(e -> searchReservations());

        // Refresh button
        refreshBtn.setOnAction(e -> refreshAllData());

        // Filter combo
        filterCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            filterReservations(newValue);
        });

        // Search field - search on Enter key
        guestSearchField.setOnAction(e -> searchReservations());
    }

    public void createReservation() {
        Guest selectedGuest = guestCombo.getValue();
        Room selectedRoom = roomCombo.getValue();
        LocalDate checkin = checkinDate.getValue();
        LocalDate checkout = checkoutDate.getValue();

        // Validate form using controller
        ReservationManagementController.ValidationResult validation =
                controller.validateReservation(selectedGuest, selectedRoom, checkin, checkout);

        if (!validation.isValid()) {
            showAlert("Validation Error", validation.getMessage());
            return;
        }

        // Create reservation using controller
        if (controller.createReservation(selectedGuest, selectedRoom, checkin, checkout)) {
            showAlert("Success", "Reservation created successfully!");
            clearForm();
            refreshAllData();
        } else {
            showAlert("Error", "Failed to create reservation. Room may no longer be available.");
        }
    }

    private void updateSelectedReservation() {
        Reservation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a reservation to update!");
            return;
        }

        Guest selectedGuest = guestCombo.getValue();
        Room selectedRoom = roomCombo.getValue();
        LocalDate checkin = checkinDate.getValue();
        LocalDate checkout = checkoutDate.getValue();

        // Validate form using controller
        ReservationManagementController.ValidationResult validation =
                controller.validateReservation(selectedGuest, selectedRoom, checkin, checkout);

        if (!validation.isValid()) {
            showAlert("Validation Error", validation.getMessage());
            return;
        }

        // Update reservation using controller
        if (controller.updateReservation(selected, selectedGuest, selectedRoom, checkin, checkout)) {
            showAlert("Success", "Reservation updated successfully!");
            refreshAllData();
        } else {
            showAlert("Error", "Failed to update reservation!");
        }
    }

    public void deleteSelectedReservation() {
        Reservation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a reservation to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setContentText("Are you sure you want to delete this reservation?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            if (controller.deleteReservation(selected.getId())) {
                showAlert("Success", "Reservation deleted successfully!");
                refreshAllData();
            } else {
                showAlert("Error", "Failed to delete reservation!");
            }
        }
    }

    private void searchReservations() {
        String searchTerm = guestSearchField.getText().trim();
        reservationList.clear();
        reservationList.addAll(controller.searchReservations(searchTerm));
        updateReservationCount();
    }

    private void filterReservations(String filterType) {
        reservationList.clear();
        reservationList.addAll(controller.filterReservations(filterType));
        updateReservationCount();
    }

    private void checkRoomAvailability() {
        Room selectedRoom = roomCombo.getValue();
        LocalDate checkin = checkinDate.getValue();
        LocalDate checkout = checkoutDate.getValue();

        if (selectedRoom != null && checkin != null && checkout != null) {
            boolean available = controller.isRoomAvailable(selectedRoom.getId(), checkin, checkout);
            if (available) {
                roomStatusLabel.setText("✅ Available");
                roomStatusLabel.getStyleClass().removeAll("status-unavailable");
                roomStatusLabel.getStyleClass().add("status-available");
            } else {
                roomStatusLabel.setText("❌ No");
                roomStatusLabel.getStyleClass().removeAll("status-available");
                roomStatusLabel.getStyleClass().add("status-unavailable");
            }
        } else {
            roomStatusLabel.setText("");
            roomStatusLabel.getStyleClass().removeAll("status-available", "status-unavailable");
        }
    }

    private void populateFormFromSelection(Reservation reservation) {
        guestCombo.setValue(reservation.getGuest());
        roomCombo.setValue(reservation.getRoom());
        checkinDate.setValue(reservation.getCheckinDate());
        checkoutDate.setValue(reservation.getCheckoutDate());
    }

    private void clearForm() {
        guestCombo.setValue(null);
        roomCombo.setValue(null);
        checkinDate.setValue(LocalDate.now());
        checkoutDate.setValue(LocalDate.now().plusDays(1));
        roomStatusLabel.setText("");
        roomStatusLabel.getStyleClass().removeAll("status-available", "status-unavailable");
        guestSearchField.clear();
    }

    private void refreshAllData() {
        loadGuestCombo();
        loadRoomCombo();
        refreshReservationTable();
    }

    private void loadGuestCombo() {
        guestCombo.getItems().clear();
        guestCombo.getItems().addAll(controller.getAllGuests());

        guestCombo.setConverter(new javafx.util.StringConverter<Guest>() {
            @Override
            public String toString(Guest guest) {
                return guest != null ? guest.getName() + " - " + guest.getContact() : "";
            }

            @Override
            public Guest fromString(String string) {
                return null;
            }
        });
    }

    private void loadRoomCombo() {
        roomCombo.getItems().clear();
        roomCombo.getItems().addAll(controller.getAvailableRooms());

        roomCombo.setConverter(new javafx.util.StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                return room != null ? "Room " + room.getNumber() + " (" + room.getType() + ")" : "";
            }

            @Override
            public Room fromString(String string) {
                return null;
            }
        });
    }

    private void refreshReservationTable() {
        String currentFilter = filterCombo.getValue();
        filterReservations(currentFilter != null ? currentFilter : "All");
    }

    private void updateReservationCount() {
        reservationCountLabel.setText("Total: " + reservationList.size());
    }

    private void openGuestManagement() {
        try {
            GuestManagement guestManagement = new GuestManagement();
            Stage guestStage = new Stage();
            guestManagement.start(guestStage);

            // Refresh reservations after the guest window is closed
            guestStage.setOnHiding(event -> {
                refreshGuestList();
            });

        } catch (Exception e) {
            showAlert("Failed to open Guest Management", e.getMessage());
        }
    }

    private void refreshGuestList() {
        guestCombo.getItems().clear();
        guestCombo.getItems().addAll(controller.getAllGuests());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}