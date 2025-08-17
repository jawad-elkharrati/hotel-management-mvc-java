package view;

import controller.GuestManagementController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Guest;
import model.GuestType;

public class GuestManagement extends Application {
    private GuestManagementController controller = new GuestManagementController();
    private TableView<Guest> table = new TableView<>();

    // Form fields
    private TextField nameField = new TextField();
    private TextField contactField = new TextField();
    private ComboBox<GuestType> guestTypeCombo = new ComboBox<>();
    private TextField searchField = new TextField();
    private Label statusLabel = new Label();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Guest Management System");
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        // Setup form fields with modern styling
        setupFormFields();

        // Create buttons with modern design
        Button addBtn = createModernButton("Add Guest", "#10B981", "M12 4.5v15m7.5-7.5h-15");
        Button updateBtn = createModernButton("Update Guest", "#3B82F6", "M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z");
        Button deleteBtn = createModernButton("Delete Guest", "#EF4444", "M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16");
        Button refreshBtn = createModernButton("Refresh", "#8B5CF6", "M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15");
        Button clearBtn = createModernButton("Clear", "#6B7280", "M6 18L18 6M6 6l12 12");
        Button searchBtn = createModernButton("Search", "#0891B2", "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z");

        // Setup table with modern design
        setupModernTable();

        // Button actions
        addBtn.setOnAction(e -> handleAddGuest());
        updateBtn.setOnAction(e -> handleUpdateGuest());
        deleteBtn.setOnAction(e -> handleDeleteGuest());
        refreshBtn.setOnAction(e -> handleRefreshTable());
        clearBtn.setOnAction(e -> handleClearFields());
        searchBtn.setOnAction(e -> handleSearchGuests());

        // Enter key actions
        nameField.setOnAction(e -> handleAddGuest());
        contactField.setOnAction(e -> handleAddGuest());
        searchField.setOnAction(e -> handleSearchGuests());

        // Table selection listener
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsFromSelection(newSelection);
                updateStatus("Selected: " + newSelection.getName() + " (" + controller.getGuestTypeFromGuest(newSelection) + ")");
            }
        });

        // Layout creation - NEW DESIGN FOCUSED ON TABLE VISIBILITY
        BorderPane layout = createTableFocusedLayout(addBtn, updateBtn, deleteBtn, refreshBtn, clearBtn, searchBtn);

        Scene scene = new Scene(layout, 1200, 800);
        scene.getStylesheets().add("data:text/css," + getModernStyles());
        stage.setScene(scene);
        stage.show();

        // Initial data load
        handleRefreshTable();
        updateStatus("Ready - Total guests: " + controller.getTotalGuestsCount());
    }

    private void setupFormFields() {
        nameField.setPromptText("Enter guest name");
        contactField.setPromptText("Enter contact information");
        searchField.setPromptText("Search by name...");

        // Setup guest type combo box
        guestTypeCombo.setItems(FXCollections.observableArrayList(GuestType.values()));
        guestTypeCombo.setValue(GuestType.REGULAR); // Default selection
        guestTypeCombo.setPromptText("Select guest type");

        // Apply modern styling
        nameField.getStyleClass().add("modern-text-field");
        contactField.getStyleClass().add("modern-text-field");
        searchField.getStyleClass().add("modern-text-field");
        guestTypeCombo.getStyleClass().add("modern-combo-box");

        nameField.setPrefHeight(40);
        contactField.setPrefHeight(40);
        searchField.setPrefHeight(40);
        guestTypeCombo.setPrefHeight(40);
    }

    private Button createModernButton(String text, String color, String iconPath) {
        Button button = new Button(text);
        button.getStyleClass().add("modern-button");
        button.setStyle("-fx-background-color: " + color + ";");
        button.setPrefHeight(40);
        button.setPrefWidth(110);
        return button;
    }

    private BorderPane createTableFocusedLayout(Button... buttons) {
        BorderPane layout = new BorderPane();
        layout.getStyleClass().add("main-container");
        layout.setPadding(new Insets(20));

        Label headerLabel = new Label("Guest Management System");
        headerLabel.getStyleClass().add("header-title");
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(headerLabel);
        headerBox.setPadding(new Insets(0, 0, 15, 0));
        layout.setTop(headerBox);

        VBox controlPanel = createCompactControlPanel(buttons);
        controlPanel.setPrefWidth(320);
        controlPanel.setMaxWidth(320);
        layout.setLeft(controlPanel);

        VBox tableSection = createExpandedTableSection();
        layout.setCenter(tableSection);

        // BOTTOM: Status bar
        statusLabel.getStyleClass().add("status-label");
        HBox statusBox = new HBox();
        statusBox.getChildren().add(statusLabel);
        statusBox.setPadding(new Insets(10, 0, 0, 0));
        layout.setBottom(statusBox);

        return layout;
    }

    private VBox createCompactControlPanel(Button... buttons) {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("control-panel");
        panel.setPadding(new Insets(0, 15, 0, 0));

        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("compact-card");
        formCard.setPadding(new Insets(20));

        Label formTitle = new Label("Guest Form");
        formTitle.getStyleClass().add("card-title");

        VBox formFields = new VBox(10);

        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("field-label");
        nameField.setPrefWidth(280);

        Label contactLabel = new Label("Contact:");
        contactLabel.getStyleClass().add("field-label");
        contactField.setPrefWidth(280);

        Label typeLabel = new Label("Guest Type:");
        typeLabel.getStyleClass().add("field-label");
        guestTypeCombo.setPrefWidth(280);

        formFields.getChildren().addAll(nameLabel, nameField, contactLabel, contactField, typeLabel, guestTypeCombo);
        formCard.getChildren().addAll(formTitle, formFields);

        VBox buttonCard = new VBox(12);
        buttonCard.getStyleClass().add("compact-card");
        buttonCard.setPadding(new Insets(20));

        Label buttonTitle = new Label("Actions");
        buttonTitle.getStyleClass().add("card-title");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(8);
        buttonGrid.setVgap(8);

        buttonGrid.add(buttons[0], 0, 0); // Add
        buttonGrid.add(buttons[1], 1, 0); // Update
        buttonGrid.add(buttons[2], 0, 1); // Delete
        buttonGrid.add(buttons[3], 1, 1); // Refresh
        buttonGrid.add(buttons[4], 0, 2); // Clear

        buttonCard.getChildren().addAll(buttonTitle, buttonGrid);

        // Search section - compact
        VBox searchCard = new VBox(12);
        searchCard.getStyleClass().add("compact-card");
        searchCard.setPadding(new Insets(20));

        Label searchTitle = new Label("Search");
        searchTitle.getStyleClass().add("card-title");

        VBox searchBox = new VBox(8);
        searchField.setPrefWidth(280);
        buttons[5].setPrefWidth(280); // Search button full width
        searchBox.getChildren().addAll(searchField, buttons[5]);

        searchCard.getChildren().addAll(searchTitle, searchBox);

        panel.getChildren().addAll(formCard, buttonCard, searchCard);
        return panel;
    }

    private VBox createExpandedTableSection() {
        VBox tableSection = new VBox(10);
        tableSection.setPadding(new Insets(0, 0, 0, 15));

        // Table header
        Label tableTitle = new Label("Guest List");
        tableTitle.getStyleClass().add("table-section-title");

        // Table container with full expansion
        VBox tableContainer = new VBox();
        tableContainer.getStyleClass().add("table-container");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        tableContainer.getChildren().add(table);

        tableSection.getChildren().addAll(tableTitle, tableContainer);
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        return tableSection;
    }

    private void setupModernTable() {
        // ID Column
        TableColumn<Guest, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);
        idCol.getStyleClass().add("table-column-center");

        // Name Column - wider for better visibility
        TableColumn<Guest, String> nameCol = new TableColumn<>("Guest Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        // Contact Column - wider for better visibility
        TableColumn<Guest, String> contactCol = new TableColumn<>("Contact Information");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setPrefWidth(250);

        // Guest Type Column
        TableColumn<Guest, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            Guest guest = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(controller.getGuestTypeFromGuest(guest));
        });
        typeCol.setPrefWidth(100);
        typeCol.getStyleClass().add("table-column-center");

        // Custom cell factory for type column to show colored badges
        typeCol.setCellFactory(column -> {
            return new TableCell<Guest, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if ("VIP".equals(item)) {
                            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                        } else {
                            setStyle("-fx-text-fill: #059669; -fx-font-weight: bold;");
                        }
                    }
                }
            };
        });

        table.getColumns().addAll(idCol, nameCol, contactCol, typeCol);
        table.setItems(controller.getGuestList());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("modern-table");

        // Enable row selection
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Remove minimum height constraint to let it expand
        table.setMinHeight(400);
    }

    // Event handlers that delegate to controller
    private void handleAddGuest() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        GuestType selectedType = guestTypeCombo.getValue();

        if (controller.addGuest(name, contact, selectedType)) {
            handleClearFields();
            updateStatus("Guest added: " + name + " (" + selectedType + ")");
        }
    }

    private void handleUpdateGuest() {
        Guest selected = table.getSelectionModel().getSelectedItem();
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        GuestType selectedType = guestTypeCombo.getValue();

        if (controller.updateGuest(selected, name, contact, selectedType)) {
            handleClearFields();
            updateStatus("Guest updated: " + name);
        }
    }

    private void handleDeleteGuest() {
        Guest selected = table.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Confirmation dialog
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete Guest");
            confirmation.setContentText("Are you sure you want to delete guest: " + selected.getName() + " (" + controller.getGuestTypeFromGuest(selected) + ")?");

            if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (controller.deleteGuest(selected)) {
                    handleClearFields();
                    updateStatus("Guest deleted: " + selected.getName());
                }
            }
        }
    }

    private void handleSearchGuests() {
        String searchTerm = searchField.getText().trim();
        controller.searchGuests(searchTerm);

        if (searchTerm.isEmpty()) {
            updateStatus("Total guests: " + controller.getGuestList().size() + " (VIP: " + controller.getVipGuestsCount() + ", Regular: " + (controller.getGuestList().size() - controller.getVipGuestsCount()) + ")");
        } else {
            updateStatus("Search results: " + controller.getGuestList().size() + " guests found");
        }
    }

    private void handleRefreshTable() {
        controller.refreshGuestList();
        updateStatus("Total guests: " + controller.getGuestList().size() + " (VIP: " + controller.getVipGuestsCount() + ", Regular: " + (controller.getGuestList().size() - controller.getVipGuestsCount()) + ")");
        searchField.clear();
    }

    private void handleClearFields() {
        nameField.clear();
        contactField.clear();
        guestTypeCombo.setValue(GuestType.REGULAR);
        table.getSelectionModel().clearSelection();
        updateStatus("Fields cleared");
    }

    private void populateFieldsFromSelection(Guest guest) {
        nameField.setText(guest.getName());
        contactField.setText(guest.getContact());
        guestTypeCombo.setValue(controller.getGuestTypeEnumFromGuest(guest));
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private String getModernStyles() {
        return """
            /* Main container with modern background */
            .main-container {
                -fx-background-color: #f8fafc;
            }
            
            /* Header styling */
            .header-title {
                -fx-font-size: 28px;
                -fx-font-weight: bold;
                -fx-text-fill: #1e293b;
            }
            
            /* Control panel styling */
            .control-panel {
                -fx-background-color: transparent;
            }
            
            /* Compact cards for controls */
            .compact-card {
                -fx-background-color: white;
                -fx-background-radius: 8px;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 1);
                -fx-border-radius: 8px;
            }
            
            /* Table section styling */
            .table-section-title {
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                -fx-text-fill: #374151;
                -fx-padding: 0 0 10 0;
            }
            
            .table-container {
                -fx-background-color: white;
                -fx-background-radius: 8px;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 1);
                -fx-border-radius: 8px;
                -fx-padding: 0;
            }
            
            /* Card titles */
            .card-title {
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-text-fill: #374151;
            }
            
            /* Field labels */
            .field-label {
                -fx-font-size: 13px;
                -fx-font-weight: 600;
                -fx-text-fill: #374151;
            }
            
            /* Modern text fields - more compact */
            .modern-text-field {
                -fx-background-color: #f9fafb;
                -fx-border-color: #d1d5db;
                -fx-border-radius: 6px;
                -fx-background-radius: 6px;
                -fx-padding: 10px 12px;
                -fx-font-size: 13px;
                -fx-text-fill: #374151;
            }
            
            .modern-text-field:focused {
                -fx-border-color: #3b82f6;
                -fx-background-color: white;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.25), 3, 0, 0, 0);
            }
            
            /* Modern combo box */
            .modern-combo-box {
                -fx-background-color: #f9fafb;
                -fx-border-color: #d1d5db;
                -fx-border-radius: 6px;
                -fx-background-radius: 6px;
                -fx-font-size: 13px;
            }
            
            .modern-combo-box:focused {
                -fx-border-color: #3b82f6;
                -fx-background-color: white;
                -fx-effect: dropshadow(gaussian, rgba(59,130,246,0.25), 3, 0, 0, 0);
            }
            
            /* Modern buttons - more compact */
            .modern-button {
                -fx-background-radius: 6px;
                -fx-border-radius: 6px;
                -fx-text-fill: white;
                -fx-font-size: 12px;
                -fx-font-weight: 600;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);
                -fx-border-width: 0;
            }
            
            .modern-button:hover {
                -fx-opacity: 0.9;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 2);
            }
            
            .modern-button:pressed {
                -fx-opacity: 0.8;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);
            }
            
            /* Enhanced table styling for better visibility */
            .modern-table {
                -fx-background-color: transparent;
                -fx-border-width: 0;
                -fx-background-radius: 8px;
            }
            
            .modern-table .column-header-background {
                -fx-background-color: #f1f5f9;
                -fx-border-color: #e2e8f0;
                -fx-border-width: 0 0 2 0;
                -fx-background-radius: 8px 8px 0 0;
            }
            
            .modern-table .column-header {
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-text-fill: #1e293b;
                -fx-padding: 15px 20px;
            }
            
            .modern-table .table-row-cell {
                -fx-background-color: white;
                -fx-border-color: #f1f5f9;
                -fx-border-width: 0 0 1 0;
                -fx-padding: 2px 0;
            }
            
            .modern-table .table-row-cell:hover {
                -fx-background-color: #f8fafc;
            }
            
            .modern-table .table-row-cell:selected {
                -fx-background-color: #dbeafe;
                -fx-text-fill: #1e40af;
            }
            
            .modern-table .table-cell {
                -fx-border-color: transparent;
                -fx-padding: 15px 20px;
                -fx-font-size: 14px;
                -fx-text-fill: #374151;
            }
            
            .table-column-center .table-cell {
                -fx-alignment: center;
                -fx-font-weight: bold;
            }
            
            /* Status label */
            .status-label {
                -fx-font-size: 14px;
                -fx-text-fill: #6b7280;
                -fx-font-style: italic;
            }
            
            /* Scroll bars */
            .scroll-bar {
                -fx-background-color: transparent;
            }
            
            .scroll-bar .track {
                -fx-background-color: #f3f4f6;
                -fx-background-radius: 4px;
            }
            
            .scroll-bar .thumb {
                -fx-background-color: #d1d5db;
                -fx-background-radius: 4px;
            }
            
            .scroll-bar .thumb:hover {
                -fx-background-color: #9ca3af;
            }
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}