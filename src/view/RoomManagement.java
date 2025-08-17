package view;

import controller.RoomManagementController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Room;
import model.RoomComponent;

import java.time.LocalDate;
import java.util.List;

public class RoomManagement extends Application {
    private RoomManagementController controller = new RoomManagementController();
    private TableView<RoomComponent> table = new TableView<>();
    private ObservableList<RoomComponent> roomList = FXCollections.observableArrayList();

    // Form fields
    private TextField numberField = new TextField();
    private ComboBox<String> typeCombo = new ComboBox<>();
    private ComboBox<String> statusCombo = new ComboBox<>();
    private TextField basePriceField = new TextField();

    // Decorator checkboxes
    private CheckBox spaCheckBox = new CheckBox("Spa Access (+$50)");
    private CheckBox minibarCheckBox = new CheckBox("Minibar (+$25)");

    // Filter fields
    private ComboBox<String> filterTypeCombo = new ComboBox<>();
    private ComboBox<String> filterStatusCombo = new ComboBox<>();
    private DatePicker checkinDatePicker = new DatePicker();
    private DatePicker checkoutDatePicker = new DatePicker();

    // Statistics labels
    private Label totalRoomsLabel = new Label();
    private Label availableRoomsLabel = new Label();
    private Label occupiedRoomsLabel = new Label();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hotel Room Management System");

        // Create main layout with modern styling
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");

        // Add header
        mainLayout.getChildren().add(createHeader());

        // Statistics panel
        mainLayout.getChildren().add(createStatisticsPanel());

        // Form panel
        mainLayout.getChildren().add(createFormPanel());

        // Filter panel
        mainLayout.getChildren().add(createFilterPanel());

        // Table
        setupTable();
        VBox tableContainer = new VBox(8);

        // Add table header
        Label tableTitle = new Label("📋 Room Inventory");
        tableTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        tableTitle.setTextFill(Color.web("#2c3e50"));
        tableTitle.setPadding(new Insets(0, 0, 8, 0));

        tableContainer.getChildren().addAll(tableTitle, table);
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 2); " +
                "-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 12;");
        tableContainer.setPadding(new Insets(15));
        mainLayout.getChildren().add(tableContainer);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1200, 800);
        stage.setScene(scene);
        stage.show();

        // Initial load
        loadRoomTypes();
        refreshTable();
        updateStatistics();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 5, 0));

        Label titleLabel = new Label("🏨 Hotel Room Management System");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);");

        header.getChildren().add(titleLabel);
        return header;
    }

    private HBox createStatisticsPanel() {
        HBox statsPanel = new HBox(20);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(15));
        statsPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Create modern stat cards
        VBox totalCard = createStatCard("📊", "Total Rooms", totalRoomsLabel, "#3498db");
        VBox availableCard = createStatCard("✅", "Available", availableRoomsLabel, "#27ae60");
        VBox occupiedCard = createStatCard("🏠", "Occupied", occupiedRoomsLabel, "#e74c3c");

        statsPanel.getChildren().addAll(totalCard, availableCard, occupiedCard);
        return statsPanel;
    }

    private VBox createStatCard(String icon, String title, Label valueLabel, String accentColor) {
        VBox card = new VBox(3);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12, 20, 12, 20));
        card.setStyle("-fx-background-color: " + accentColor + "; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 1);");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        titleLabel.setTextFill(Color.WHITE);

        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel);
        return card;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(12);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label formTitle = new Label("Room Information");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        formTitle.setTextFill(Color.web("#2c3e50"));

        // Setup form fields with modern styling
        styleTextField(numberField, "Room Number");
        styleComboBox(typeCombo, "Room Type");
        styleComboBox(statusCombo, "Status");
        styleTextField(basePriceField, "Base Price");

        typeCombo.getItems().addAll("Single", "Double", "Suite", "Deluxe", "Executive", "Presidential");
        statusCombo.getItems().addAll("Available", "Occupied", "Maintenance", "Out of Order", "Reserved");

        // Style checkboxes
        spaCheckBox.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");
        minibarCheckBox.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        HBox formFields = new HBox(12);
        formFields.setAlignment(Pos.CENTER);
        formFields.getChildren().addAll(numberField, typeCombo, statusCombo, basePriceField);

        // Decorator options
        HBox decoratorBox = new HBox(20);
        decoratorBox.setAlignment(Pos.CENTER);
        decoratorBox.getChildren().addAll(spaCheckBox, minibarCheckBox);

        // Modern buttons
        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        Button addBtn = createModernButton("➕ Add Room", "#27ae60");
        Button updateBtn = createModernButton("✏️ Update", "#3498db");
        Button deleteBtn = createModernButton("🗑️ Delete", "#e74c3c");
        Button clearBtn = createModernButton("🔄 Clear", "#95a5a6");

        buttonBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);

        // Button actions
        setupButtonActions(addBtn, updateBtn, deleteBtn, clearBtn);

        formPanel.getChildren().addAll(formTitle, formFields, decoratorBox, buttonBox);
        return formPanel;
    }

    private VBox createFilterPanel() {
        VBox filterPanel = new VBox(12);
        filterPanel.setPadding(new Insets(20));
        filterPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        Label filterTitle = new Label("🔍 Filter & Search Options");
        filterTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        filterTitle.setTextFill(Color.web("#2c3e50"));

        // First row of filters
        HBox filterRow1 = new HBox(12);
        filterRow1.setAlignment(Pos.CENTER);

        styleComboBox(filterTypeCombo, "Filter by Type");
        styleComboBox(filterStatusCombo, "Filter by Status");
        filterStatusCombo.getItems().addAll("All", "Available", "Occupied", "Maintenance", "Out of Order", "Reserved");

        Button showAllBtn = createModernButton("📋 Show All", "#34495e");
        Button showAvailableBtn = createModernButton("✅ Available", "#27ae60");
        Button refreshBtn = createModernButton("🔄 Refresh", "#f39c12");

        filterRow1.getChildren().addAll(filterTypeCombo, filterStatusCombo, showAllBtn, showAvailableBtn, refreshBtn);

        // Second row of filters
        HBox filterRow2 = new HBox(12);
        filterRow2.setAlignment(Pos.CENTER);

        styleDatePicker(checkinDatePicker, "Check-in Date");
        styleDatePicker(checkoutDatePicker, "Check-out Date");

        Button searchByDateBtn = createModernButton("📅 Search by Date", "#9b59b6");

        filterRow2.getChildren().addAll(checkinDatePicker, checkoutDatePicker, searchByDateBtn);

        // Filter actions
        setupFilterActions(showAllBtn, showAvailableBtn, refreshBtn, searchByDateBtn);

        filterPanel.getChildren().addAll(filterTitle, filterRow1, filterRow2);
        return filterPanel;
    }

    private void setupTable() {
        // Modern table styling with better visibility
        table.setStyle("-fx-background-color: white; -fx-table-cell-border-color: #dee2e6; " +
                "-fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 8; " +
                "-fx-background-radius: 8;");

        // Set table column resize policy to fill the entire width
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RoomComponent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMaxWidth(80);
        idCol.setMinWidth(60);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RoomComponent, String> numberCol = new TableColumn<>("Room Number");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberCol.setMinWidth(120);
        numberCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RoomComponent, String> typeCol = new TableColumn<>("Room Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setMinWidth(120);
        typeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RoomComponent, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setMinWidth(120);
        statusCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RoomComponent, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionCol.setMinWidth(200);
        descriptionCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<RoomComponent, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> {
            RoomComponent room = cellData.getValue();
            double totalPrice = controller.calculateTotalPrice(room);
            return new javafx.beans.property.SimpleStringProperty(String.format("$%.2f", totalPrice));
        });

        // Enhanced status column with modern styling
        statusCol.setCellFactory(column -> new TableCell<RoomComponent, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    setAlignment(Pos.CENTER);
                    setFont(Font.font("System", FontWeight.BOLD, 12));

                    switch (status) {
                        case "Available":
                            setStyle("-fx-background-color: #d5edda; -fx-text-fill: #155724; " +
                                    "-fx-background-radius: 15; -fx-padding: 5;");
                            break;
                        case "Occupied":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24; " +
                                    "-fx-background-radius: 15; -fx-padding: 5;");
                            break;
                        case "Maintenance":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; " +
                                    "-fx-background-radius: 15; -fx-padding: 5;");
                            break;
                        case "Out of Order":
                            setStyle("-fx-background-color: #f5c6cb; -fx-text-fill: #491217; " +
                                    "-fx-background-radius: 15; -fx-padding: 5;");
                            break;
                        case "Reserved":
                            setStyle("-fx-background-color: #cce7ff; -fx-text-fill: #004085; " +
                                    "-fx-background-radius: 15; -fx-padding: 5;");
                            break;
                        default:
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Style column headers
        for (TableColumn<RoomComponent, ?> column : List.of(idCol, numberCol, typeCol, statusCol, descriptionCol, priceCol)) {
            column.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #2c3e50; " +
                    "-fx-font-weight: bold; -fx-font-size: 14px; " +
                    "-fx-border-color: #dee2e6; -fx-border-width: 0 0 2 0;");
        }

        table.getColumns().addAll(idCol, numberCol, typeCol, statusCol, descriptionCol, priceCol);
        table.setItems(roomList);

        // Enhanced row styling for better visibility
        table.setRowFactory(tv -> {
            TableRow<RoomComponent> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-border-color: #e9ecef; " +
                    "-fx-border-width: 0 0 1 0; -fx-font-size: 13px;");

            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f1f3f4; -fx-border-color: #e9ecef; " +
                            "-fx-border-width: 0 0 1 0; -fx-font-size: 13px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);");
                }
            });

            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: white; -fx-border-color: #e9ecef; " +
                            "-fx-border-width: 0 0 1 0; -fx-font-size: 13px;");
                }
            });

            return row;
        });

        // Table selection listener
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void styleTextField(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setPrefWidth(160);
        field.setPrefHeight(35);
        field.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6; -fx-font-size: 13px;");

        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                field.setStyle("-fx-background-color: white; -fx-border-color: #3498db; " +
                        "-fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6; " +
                        "-fx-padding: 6; -fx-font-size: 13px;");
            } else {
                field.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                        "-fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; " +
                        "-fx-padding: 6; -fx-font-size: 13px;");
            }
        });
    }

    private void styleComboBox(ComboBox<String> combo, String prompt) {
        combo.setPromptText(prompt);
        combo.setPrefWidth(160);
        combo.setPrefHeight(35);
        combo.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6; -fx-font-size: 13px;");
    }

    private void styleDatePicker(DatePicker picker, String prompt) {
        picker.setPromptText(prompt);
        picker.setPrefWidth(160);
        picker.setPrefHeight(35);
        picker.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; " +
                "-fx-padding: 6; -fx-font-size: 13px;");
    }

    private Button createModernButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefHeight(35);
        button.setPrefWidth(110);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; " +
                "-fx-cursor: hand; -fx-font-size: 11px;");

        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: derive(" + color + ", -10%); -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-background-radius: 6; " +
                    "-fx-cursor: hand; -fx-font-size: 11px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 1);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-background-radius: 6; " +
                    "-fx-cursor: hand; -fx-font-size: 11px;");
        });

        return button;
    }

    private void setupButtonActions(Button addBtn, Button updateBtn, Button deleteBtn, Button clearBtn) {
        addBtn.setOnAction(e -> addRoom());
        updateBtn.setOnAction(e -> updateRoom());
        deleteBtn.setOnAction(e -> deleteRoom());
        clearBtn.setOnAction(e -> clearForm());
    }

    private void setupFilterActions(Button showAllBtn, Button showAvailableBtn, Button refreshBtn, Button searchByDateBtn) {
        showAllBtn.setOnAction(e -> showAllRooms());
        showAvailableBtn.setOnAction(e -> showAvailableRooms());
        refreshBtn.setOnAction(e -> {
            refreshTable();
            updateStatistics();
        });
        searchByDateBtn.setOnAction(e -> searchByDateRange());

        // Filter combo box listeners
        filterTypeCombo.setOnAction(e -> filterByType());
        filterStatusCombo.setOnAction(e -> filterByStatus());
    }

    private void addRoom() {
        if (!controller.validateInput(numberField.getText(), typeCombo.getValue(),
                statusCombo.getValue(), basePriceField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", getValidationErrorMessage());
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(basePriceField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid price!");
            return;
        }

        if (controller.addRoom(numberField.getText(), typeCombo.getValue(), statusCombo.getValue(),
                basePrice, spaCheckBox.isSelected(), minibarCheckBox.isSelected())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully!");
            clearForm();
            refreshTable();
            updateStatistics();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add room! Room number may already exist.");
        }
    }

    private void updateRoom() {
        RoomComponent selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a room to update!");
            return;
        }

        if (!controller.validateInput(numberField.getText(), typeCombo.getValue(),
                statusCombo.getValue(), basePriceField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", getValidationErrorMessage());
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(basePriceField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid price!");
            return;
        }

        if (controller.updateRoom(selected, numberField.getText(), typeCombo.getValue(),
                statusCombo.getValue(), basePrice, spaCheckBox.isSelected(),
                minibarCheckBox.isSelected())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully!");
            clearForm();
            refreshTable();
            updateStatistics();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update room! Room number may already exist.");
        }
    }

    private void deleteRoom() {
        RoomComponent selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a room to delete!");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Room");
        confirmation.setContentText("Are you sure you want to delete room: " + selected.getNumber() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (controller.deleteRoom(selected.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Room deleted successfully!");
                clearForm();
                refreshTable();
                updateStatistics();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete room!");
            }
        }
    }

    private void clearForm() {
        numberField.clear();
        typeCombo.setValue(null);
        statusCombo.setValue(null);
        basePriceField.clear();
        spaCheckBox.setSelected(false);
        minibarCheckBox.setSelected(false);
        table.getSelectionModel().clearSelection();
    }

    private void populateForm(RoomComponent room) {
        numberField.setText(room.getNumber());
        typeCombo.setValue(room.getType());
        statusCombo.setValue(room.getStatus());

        // Get base room to get the base price
        Room baseRoom = controller.getBaseRoom(room);
        basePriceField.setText(String.valueOf(baseRoom.getBasePrice()));

        // Check decorators based on description
        String description = room.getDescription();
        spaCheckBox.setSelected(description != null && description.contains("Spa Access"));
        minibarCheckBox.setSelected(description != null && description.contains("Minibar"));
    }

    private void showAllRooms() {
        refreshTable();
        filterTypeCombo.setValue(null);
        filterStatusCombo.setValue("All");
    }

    private void showAvailableRooms() {
        List<Room> availableRooms = controller.getAvailableRooms();
        roomList.clear();
        roomList.addAll(availableRooms);
        filterStatusCombo.setValue("Available");
    }

    private void filterByType() {
        String selectedType = filterTypeCombo.getValue();
        List<Room> filteredRooms = controller.filterByType(selectedType);
        roomList.clear();
        roomList.addAll(filteredRooms);
    }

    private void filterByStatus() {
        String selectedStatus = filterStatusCombo.getValue();
        List<Room> filteredRooms = controller.filterByStatus(selectedStatus);
        roomList.clear();
        roomList.addAll(filteredRooms);
    }

    private void searchByDateRange() {
        LocalDate checkin = checkinDatePicker.getValue();
        LocalDate checkout = checkoutDatePicker.getValue();

        if (!controller.isValidDateRange(checkin, checkout)) {
            showAlert(Alert.AlertType.WARNING, "Date Error",
                    "Please select valid check-in and check-out dates!\nCheck-in must be before or equal to check-out.");
            return;
        }

        List<Room> availableRooms = controller.searchByDateRange(checkin, checkout);
        roomList.clear();
        roomList.addAll(availableRooms);

        showAlert(Alert.AlertType.INFORMATION, "Search Results",
                "Found " + availableRooms.size() + " available rooms for the selected date range.");
    }

    private void refreshTable() {
        List<Room> allRooms = controller.getAllRooms();
        roomList.clear();
        roomList.addAll(allRooms);

        // Clear date pickers
        checkinDatePicker.setValue(null);
        checkoutDatePicker.setValue(null);
    }

    private void loadRoomTypes() {
        List<String> roomTypes = controller.getAllRoomTypes();
        filterTypeCombo.getItems().clear();
        filterTypeCombo.getItems().add("All");
        filterTypeCombo.getItems().addAll(roomTypes);
        filterTypeCombo.setValue("All");
    }

    private void updateStatistics() {
        totalRoomsLabel.setText(String.valueOf(controller.getTotalRoomsCount()));
        availableRoomsLabel.setText(String.valueOf(controller.getAvailableRoomsCount()));
        occupiedRoomsLabel.setText(String.valueOf(controller.getOccupiedRoomsCount()));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        alert.showAndWait();
    }

    private String getValidationErrorMessage() {
        StringBuilder message = new StringBuilder("Please fix the following issues:\n");

        if (numberField.getText() == null || numberField.getText().trim().isEmpty()) {
            message.append("• Room number is required\n");
        }

        if (typeCombo.getValue() == null) {
            message.append("• Room type must be selected\n");
        }

        if (statusCombo.getValue() == null) {
            message.append("• Room status must be selected\n");
        }

        if (basePriceField.getText() == null || basePriceField.getText().trim().isEmpty()) {
            message.append("• Base price is required\n");
        } else if (!controller.isValidPrice(basePriceField.getText())) {
            message.append("• Base price must be a positive number\n");
        }

        return message.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}