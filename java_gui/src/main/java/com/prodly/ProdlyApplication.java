package com.prodly;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Screen;

/**
 * Main Application Class
 * Professional SaaS Vendor Lock-In Analysis Dashboard
 */
public class ProdlyApplication extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    // Navigation
    private HBox navigationBar;
    
    // Screen managers
    private VendorLockInScreen vendorLockInScreen;
    private MigrationDifficultyScreen migrationDifficultyScreen;
    private ExitReadinessScreen exitReadinessScreen;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        initRootLayout();
        showVendorLockInScreen();
    }
    
    private void initRootLayout() {
        rootLayout = new BorderPane();
        rootLayout.getStyleClass().add("root-layout");
        
        // Create navigation bar
        createNavigationBar();
        rootLayout.setTop(navigationBar);
        
        // Get screen dimensions for proper sizing
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        
        // Use screen dimensions but not fullscreen (keeps window controls)
        double windowWidth = Math.min(1400, screenWidth * 0.95);
        double windowHeight = Math.min(900, screenHeight * 0.9);
        
        Scene scene = new Scene(rootLayout, windowWidth, windowHeight);
        
        // Load CSS from resources
        try {
            String cssUrl = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssUrl);
        } catch (Exception e) {
            System.err.println("Warning: Could not load styles.css: " + e.getMessage());
        }
        
        // Configure stage properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("Prodly - Vendor Lock-In Analysis Platform");
        
        // Window controls - ensure they are visible
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        
        // Ensure window controls are visible (not fullscreen)
        primaryStage.setFullScreen(false);
        
        // Center window on screen
        primaryStage.centerOnScreen();
        
        // Start maximized (but keeps window controls visible)
        primaryStage.setMaximized(true);
        
        // Show the stage
        primaryStage.show();
    }
    
    private void createNavigationBar() {
        navigationBar = new HBox(20);
        navigationBar.setAlignment(Pos.CENTER_LEFT);
        navigationBar.setPadding(new Insets(20, 30, 20, 30));
        navigationBar.getStyleClass().add("navigation-bar");
        
        // Logo/Brand
        Label logo = new Label("PRODLY");
        logo.getStyleClass().add("logo");
        
        // Navigation buttons
        Button btnVendorLockIn = createNavButton("Vendor Lock-In Score", () -> showVendorLockInScreen());
        Button btnMigration = createNavButton("Migration Difficulty", () -> showMigrationDifficultyScreen());
        Button btnExitReadiness = createNavButton("Exit Readiness", () -> showExitReadinessScreen());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        navigationBar.getChildren().addAll(logo, btnVendorLockIn, btnMigration, btnExitReadiness, spacer);
    }
    
    private Button createNavButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setOnAction(e -> action.run());
        return button;
    }
    
    private void showVendorLockInScreen() {
        if (vendorLockInScreen == null) {
            vendorLockInScreen = new VendorLockInScreen(this);
        }
        ScrollPane scrollPane = createScrollPane(vendorLockInScreen.getView());
        rootLayout.setCenter(scrollPane);
        updateActiveNavButton("Vendor Lock-In Score");
    }
    
    private void showMigrationDifficultyScreen() {
        if (migrationDifficultyScreen == null) {
            migrationDifficultyScreen = new MigrationDifficultyScreen(this);
        }
        ScrollPane scrollPane = createScrollPane(migrationDifficultyScreen.getView());
        rootLayout.setCenter(scrollPane);
        updateActiveNavButton("Migration Difficulty");
    }
    
    private void showExitReadinessScreen() {
        if (exitReadinessScreen == null) {
            exitReadinessScreen = new ExitReadinessScreen(this);
        }
        ScrollPane scrollPane = createScrollPane(exitReadinessScreen.getView());
        rootLayout.setCenter(scrollPane);
        updateActiveNavButton("Exit Readiness");
    }
    
    private ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false); // Allow VBox to determine height for scrolling
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(false);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }
    
    private void updateActiveNavButton(String activeText) {
        navigationBar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().equals(activeText)) {
                    btn.getStyleClass().add("nav-button-active");
                } else {
                    btn.getStyleClass().remove("nav-button-active");
                }
            }
        });
    }
    
    public void showToast(String message, String type) {
        // Toast notification implementation
        // This would show a temporary notification
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
