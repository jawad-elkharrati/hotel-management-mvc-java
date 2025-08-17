package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MainMenu extends Application {

    private Stage primaryStage;
    private Label timeLabel;
    private Label statusLabel;
    private Label dateLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hotel Management System");
        primaryStage.setResizable(false);

        // Create the main layout
        BorderPane mainLayout = createMainLayout();

        // Create scene with modern styling
        Scene scene = new Scene(mainLayout, 900, 700);
        scene.getStylesheets().add("data:text/css," + getStyles());

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Start the clock
        startClock();
        updateStatus("System ready");
    }

    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-container");

        // Header section
        VBox headerSection = createHeaderSection();

        // Center section with cards
        HBox centerSection = createCenterSection();

        // Footer section
        HBox footerSection = createFooterSection();

        mainLayout.setTop(headerSection);
        mainLayout.setCenter(centerSection);
        mainLayout.setBottom(footerSection);

        return mainLayout;
    }

    private VBox createHeaderSection() {
        VBox headerSection = new VBox();
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setSpacing(8);
        headerSection.setPadding(new Insets(40, 30, 30, 30));
        headerSection.getStyleClass().add("header-section");

        // Logo and title container
        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setSpacing(15);

        // Hotel icon
        Label hotelIcon = new Label("🏨");
        hotelIcon.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        hotelIcon.getStyleClass().add("hotel-icon");

        // Title section
        VBox titleSection = new VBox();
        titleSection.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("DATA HOTELS");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Management System");
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        subtitleLabel.getStyleClass().add("subtitle-label");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);
        titleContainer.getChildren().addAll(hotelIcon, titleSection);

        // Time and date section
        VBox timeSection = new VBox();
        timeSection.setAlignment(Pos.CENTER);
        timeSection.setSpacing(2);
        timeSection.setPadding(new Insets(15, 0, 0, 0));

        timeLabel = new Label();
        timeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        timeLabel.getStyleClass().add("time-label");

        dateLabel = new Label();
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        dateLabel.getStyleClass().add("date-label");

        timeSection.getChildren().addAll(timeLabel, dateLabel);

        headerSection.getChildren().addAll(titleContainer, timeSection);
        return headerSection;
    }

    private HBox createCenterSection() {
        HBox centerSection = new HBox();
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setSpacing(30);
        centerSection.setPadding(new Insets(20, 50, 40, 50));

        // Create management cards
        VBox roomCard = createManagementCard("🏠", "Room\nManagement", "Manage hotel rooms\nand availability", this::openRoomManagement);
        VBox guestCard = createManagementCard("👥", "Guest\nManagement", "Manage guest information\nand profiles", this::openGuestManagement);
        VBox reservationCard = createManagementCard("📋", "Reservation\nManagement", "Handle bookings\nand reservations", this::openReservationManagement);

        centerSection.getChildren().addAll(roomCard, guestCard, reservationCard);
        return centerSection;
    }

    private VBox createManagementCard(String icon, String title, String description, Runnable action) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(15);
        card.setPadding(new Insets(30, 25, 30, 25));
        card.setPrefWidth(220);
        card.setPrefHeight(280);
        card.getStyleClass().add("management-card");

        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        iconLabel.getStyleClass().add("card-icon");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.getStyleClass().add("card-title");
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Description
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        descLabel.getStyleClass().add("card-description");
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        descLabel.setWrapText(true);

        // Access button
        Button accessBtn = new Button("ACCESS");
        accessBtn.setPrefWidth(120);
        accessBtn.setPrefHeight(35);
        accessBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        accessBtn.getStyleClass().add("access-button");
        accessBtn.setOnAction(e -> action.run());

        // Add hover effects
        card.setOnMouseEntered(e -> card.getStyleClass().add("management-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("management-card-hover"));

        card.getChildren().addAll(iconLabel, titleLabel, descLabel, accessBtn);
        return card;
    }

    private HBox createFooterSection() {
        HBox footerSection = new HBox();
        footerSection.setAlignment(Pos.CENTER);
        footerSection.setSpacing(30);
        footerSection.setPadding(new Insets(20, 40, 30, 40));
        footerSection.getStyleClass().add("footer-section");

        // Status section
        HBox statusSection = new HBox();
        statusSection.setAlignment(Pos.CENTER_LEFT);
        statusSection.setSpacing(8);

        Label statusIcon = new Label("●");
        statusIcon.getStyleClass().add("status-indicator");

        statusLabel = new Label("Ready");
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        statusLabel.getStyleClass().add("status-label");

        statusSection.getChildren().addAll(statusIcon, statusLabel);

        // Exit button
        Button exitBtn = new Button("EXIT SYSTEM");
        exitBtn.setPrefWidth(120);
        exitBtn.setPrefHeight(35);
        exitBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        exitBtn.getStyleClass().add("exit-button");
        exitBtn.setOnAction(e -> exitApplication());

        // Version info
        Label versionLabel = new Label("Version 1.1.0");
        versionLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        versionLabel.getStyleClass().add("version-label");

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        footerSection.getChildren().addAll(statusSection, leftSpacer, exitBtn, rightSpacer, versionLabel);
        return footerSection;
    }

    private void openRoomManagement() {
        try {
            updateStatus("Opening Room Management...");
            RoomManagement roomManagement = new RoomManagement();
            Stage roomStage = new Stage();
            roomManagement.start(roomStage);
            updateStatus("Room Management opened");
        } catch (Exception e) {
            showError("Failed to open Room Management", e.getMessage());
            updateStatus("Error opening Room Management");
        }
    }

    private void openGuestManagement() {
        try {
            updateStatus("Opening Guest Management...");
            GuestManagement guestManagement = new GuestManagement();
            Stage guestStage = new Stage();
            guestManagement.start(guestStage);
            updateStatus("Guest Management opened");
        } catch (Exception e) {
            showError("Failed to open Guest Management", e.getMessage());
            updateStatus("Error opening Guest Management");
        }
    }

    private void openReservationManagement() {
        try {
            updateStatus("Opening Reservation Management...");
            ReservationManagement reservationManagement = new ReservationManagement();
            Stage reservationStage = new Stage();
            reservationManagement.start(reservationStage);
            updateStatus("Reservation Management opened");
        } catch (Exception e) {
            showError("Failed to open Reservation Management", e.getMessage());
            updateStatus("Error opening Reservation Management");
        }
    }

    private void exitApplication() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Exit Application");
        confirmAlert.setHeaderText("Confirm Exit");
        confirmAlert.setContentText("Are you sure you want to exit the Hotel Management System?");

        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmAlert.getButtonTypes().setAll(exitButton, cancelButton);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == exitButton) {
            updateStatus("Shutting down system...");
            Platform.exit();
            System.exit(0);
        }
    }

    private void startClock() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
                        timeLabel.setText(now.format(timeFormatter));
                        dateLabel.setText(now.format(dateFormatter));
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        clockThread.setDaemon(true);
        clockThread.start();
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getStyles() {
        return """
            .main-container {
                -fx-background-color: #667eea;
            }
            
            .header-section {
                -fx-background-color: #ffffff;
                -fx-background-radius: 0 0 25 25;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 5);
            }
            
            .hotel-icon {
                -fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.4), 8, 0, 0, 3);
            }
            
            .title-label {
                -fx-text-fill: #667eea;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);
            }
            
            .subtitle-label {
                -fx-text-fill: #64748b;
            }
            
            .time-label {
                -fx-text-fill: #1e293b;
            }
            
            .date-label {
                -fx-text-fill: #64748b;
            }
            
            .management-card {
                -fx-background-color: #ffffff;
                -fx-background-radius: 20;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 20, 0, 0, 10);
                -fx-cursor: hand;
            }
            
            .management-card:hover, .management-card-hover {
                -fx-background-color: #ffffff;
                -fx-scale-x: 1.05;
                -fx-scale-y: 1.05;
                -fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.3), 25, 0, 0, 15);
            }
            
            .card-icon {
                -fx-text-fill: #667eea;
                -fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.3), 5, 0, 0, 2);
            }
            
            .card-title {
                -fx-text-fill: #1e293b;
                -fx-text-alignment: center;
            }
            
            .card-description {
                -fx-text-fill: #64748b;
                -fx-text-alignment: center;
            }
            
            .access-button {
                -fx-background-color: #667eea;
                -fx-text-fill: white;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.4), 8, 0, 0, 3);
                -fx-cursor: hand;
            }
            
            .access-button:hover {
                -fx-background-color: #5a67d8;
                -fx-scale-x: 1.05;
                -fx-scale-y: 1.05;
                -fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.6), 12, 0, 0, 5);
            }
            
            .access-button:pressed {
                -fx-scale-x: 0.95;
                -fx-scale-y: 0.95;
            }
            
            .footer-section {
                -fx-background-color: #ffffff;
                -fx-background-radius: 25 25 0 0;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, -5);
            }
            
            .status-indicator {
                -fx-text-fill: #10b981;
                -fx-font-size: 8px;
            }
            
            .status-label {
                -fx-text-fill: #374151;
            }
            
            .exit-button {
                -fx-background-color: #ef4444;
                -fx-text-fill: white;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.4), 8, 0, 0, 3);
                -fx-cursor: hand;
            }
            
            .exit-button:hover {
                -fx-background-color: #dc2626;
                -fx-scale-x: 1.05;
                -fx-scale-y: 1.05;
                -fx-effect: dropshadow(three-pass-box, rgba(239,68,68,0.6), 12, 0, 0, 5);
            }
            
            .exit-button:pressed {
                -fx-scale-x: 0.95;
                -fx-scale-y: 0.95;
            }
            
            .version-label {
                -fx-text-fill: #9ca3af;
            }
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
}