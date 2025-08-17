package view;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage extends Application {

    // Default credentials (in real application, this would be in database)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management System - Login");

        // Create main container with gradient background
        StackPane root = new StackPane();

        // Static background gradient
        Rectangle backgroundRect = new Rectangle(600, 400);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.5, Color.web("#764ba2")),
                new Stop(1, Color.web("#f093fb"))
        );
        backgroundRect.setFill(gradient);

        // Create main login card
        VBox loginCard = new VBox(20);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(40, 50, 40, 50));
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(500);

        // Clean card design with good visibility
        loginCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.8);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);"
        );

        // Floating animation for login card
        TranslateTransition floatTransition = new TranslateTransition(Duration.seconds(3), loginCard);
        floatTransition.setFromY(-5);
        floatTransition.setToY(5);
        floatTransition.setCycleCount(Timeline.INDEFINITE);
        floatTransition.setAutoReverse(true);
        floatTransition.play();

        // Title with clear visibility
        Label titleLabel = new Label("🏨 Hotel Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.web("#667eea", 0.6));
        titleShadow.setRadius(5);
        titleLabel.setEffect(titleShadow);

        Label subtitleLabel = new Label("Welcome Back");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.web("#34495e"));

        // Animated welcome text
        FadeTransition titleFade = new FadeTransition(Duration.seconds(2), titleLabel);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);
        titleFade.play();

        // Custom styled input fields with clear visibility
        VBox inputContainer = new VBox(15);
        inputContainer.setAlignment(Pos.CENTER);

        // Username field with icon
        HBox usernameBox = new HBox(10);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label userIcon = new Label("👤");
        userIcon.setFont(Font.font(18));

        TextField userTextField = new TextField();
        userTextField.setPromptText("Enter username");
        userTextField.setPrefWidth(250);
        userTextField.setPrefHeight(45);
        userTextField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 2;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Focus animations for username field
        userTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), userTextField);
            if (newVal) {
                scale.setToX(1.05);
                scale.setToY(1.05);
                userTextField.setStyle(userTextField.getStyle() + "-fx-border-color: #667eea; -fx-border-width: 2;");
            } else {
                scale.setToX(1.0);
                scale.setToY(1.0);
                userTextField.setStyle(userTextField.getStyle().replace("-fx-border-color: #667eea; -fx-border-width: 2;", ""));
            }
            scale.play();
        });

        usernameBox.getChildren().addAll(userIcon, userTextField);

        // Password field with icon
        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passIcon = new Label("🔒");
        passIcon.setFont(Font.font(18));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(250);
        passwordField.setPrefHeight(45);
        passwordField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 2;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Focus animations for password field
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), passwordField);
            if (newVal) {
                scale.setToX(1.05);
                scale.setToY(1.05);
                passwordField.setStyle(passwordField.getStyle() + "-fx-border-color: #667eea; -fx-border-width: 2;");
            } else {
                scale.setToX(1.0);
                scale.setToY(1.0);
                passwordField.setStyle(passwordField.getStyle().replace("-fx-border-color: #667eea; -fx-border-width: 2;", ""));
            }
            scale.play();
        });

        passwordBox.getChildren().addAll(passIcon, passwordField);
        inputContainer.getChildren().addAll(usernameBox, passwordBox);

        // Buttons container
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);

        // Styled login button with gradient and animations
        Button loginBtn = new Button("Login ✨");
        loginBtn.setPrefWidth(120);
        loginBtn.setPrefHeight(45);
        loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"
        );

        // Hover animations for login button
        loginBtn.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), loginBtn);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();

            loginBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5a6fd8, #6a42a0);" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 6);"
            );
        });

        loginBtn.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), loginBtn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            loginBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"
            );
        });

        // Styled clear button
        Button clearBtn = new Button("Clear 🗑");
        clearBtn.setPrefWidth(120);
        clearBtn.setPrefHeight(45);
        clearBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clearBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.8);" +
                        "-fx-text-fill: #666;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        // Hover animations for clear button
        clearBtn.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), clearBtn);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        clearBtn.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), clearBtn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        buttonContainer.getChildren().addAll(clearBtn, loginBtn);

        // Info label with good visibility
        Label infoLabel = new Label("💡 Default: admin / admin123");
        infoLabel.setFont(Font.font("Arial", 12));
        infoLabel.setTextFill(Color.web("#7f8c8d"));

        FadeTransition infoPulse = new FadeTransition(Duration.seconds(2), infoLabel);
        infoPulse.setFromValue(0.5);
        infoPulse.setToValue(1.0);
        infoPulse.setCycleCount(Timeline.INDEFINITE);
        infoPulse.setAutoReverse(true);
        infoPulse.play();

        // Add all elements to login card
        loginCard.getChildren().addAll(titleLabel, subtitleLabel, inputContainer, buttonContainer, infoLabel);

        // Button actions with animations
        loginBtn.setOnAction(e -> {
            // Login button click animation
            ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), loginBtn);
            clickScale.setToX(0.95);
            clickScale.setToY(0.95);
            clickScale.setOnFinished(event -> {
                ScaleTransition backScale = new ScaleTransition(Duration.millis(100), loginBtn);
                backScale.setToX(1.0);
                backScale.setToY(1.0);
                backScale.play();
            });
            clickScale.play();

            String username = userTextField.getText();
            String password = passwordField.getText();

            if (validateLogin(username, password)) {
                // Success animation
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), loginCard);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> {
                    primaryStage.close();
                    // Open main menu
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.start(new Stage());
                });
                fadeOut.play();
            } else {
                // Shake animation for error
                TranslateTransition shake = new TranslateTransition(Duration.millis(100), loginCard);
                shake.setFromX(0);
                shake.setToX(10);
                shake.setCycleCount(6);
                shake.setAutoReverse(true);
                shake.setOnFinished(event -> showAlert("Login Failed", "Invalid username or password! ❌"));
                shake.play();

                passwordField.clear();
            }
        });

        clearBtn.setOnAction(e -> {
            // Clear animation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), inputContainer);
            fadeOut.setToValue(0.3);
            fadeOut.setOnFinished(event -> {
                userTextField.clear();
                passwordField.clear();
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), inputContainer);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });

        // Allow Enter key to login
        passwordField.setOnAction(e -> loginBtn.fire());

        // Add components to root
        root.getChildren().addAll(backgroundRect, loginCard);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Initial entrance animation
        ScaleTransition entrance = new ScaleTransition(Duration.millis(800), loginCard);
        entrance.setFromX(0.8);
        entrance.setFromY(0.8);
        entrance.setToX(1.0);
        entrance.setToY(1.0);
        entrance.play();

        // Focus on username field after animation
        entrance.setOnFinished(e -> userTextField.requestFocus());
    }

    private boolean validateLogin(String username, String password) {
        return DEFAULT_USERNAME.equals(username) && DEFAULT_PASSWORD.equals(password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #667eea, #764ba2);" +
                        "-fx-text-fill: white;"
        );

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}