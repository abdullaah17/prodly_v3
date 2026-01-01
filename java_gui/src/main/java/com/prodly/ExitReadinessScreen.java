package com.prodly;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.text.DecimalFormat;

/**
 * Module 3: Exit Readiness Dashboard Screen
 * Professional UI with charts and sorted vendor analysis
 */
public class ExitReadinessScreen {
    private ProdlyApplication app;
    private VBox root;
    private ExitReadinessDashboardJNI dashboard;
    private TableView<VendorReadiness> readinessTable;
    
    public ExitReadinessScreen(ProdlyApplication app) {
        this.app = app;
        this.dashboard = new ExitReadinessDashboardJNI();
        createUI();
    }
    
    private void createUI() {
        root = new VBox(30);
        root.setPadding(new Insets(40, 50, 40, 50));
        root.getStyleClass().add("screen-container");
        
        // Header
        VBox header = createHeader("Exit Readiness Dashboard",
            "Analyze vendor exit readiness and optimal migration paths");
        
        // Main content
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left: Input Form
        VBox leftPanel = createInputPanel();
        leftPanel.setPrefWidth(450);
        
        // Right: Results and Charts
        VBox rightPanel = createResultsPanel();
        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        
        root.getChildren().addAll(header, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        
        // Load initial data
        refreshTable();
    }
    
    private VBox createHeader(String title, String subtitle) {
        VBox header = new VBox(10);
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("screen-title");
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("screen-subtitle");
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private VBox createInputPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("form-section");
        panel.setPadding(new Insets(30));
        
        Label sectionTitle = new Label("Add Vendor Metrics");
        sectionTitle.getStyleClass().add("section-title");
        
        TextField vendorIdField = createTextField("Vendor ID");
        TextField lockInScoreField = createTextField("Lock-In Score (0-100)");
        TextField migrationDifficultyField = createTextField("Migration Difficulty (0-100)");
        TextField dataExportField = createTextField("Data Export Capability (0-100)");
        TextField contractFlexibilityField = createTextField("Contract Flexibility (0-100)");
        TextField technicalComplexityField = createTextField("Technical Complexity (0-100)");
        
        Button calculateButton = new Button("Calculate Exit Readiness");
        calculateButton.getStyleClass().add("primary-button");
        calculateButton.setPrefWidth(Double.MAX_VALUE);
        calculateButton.setOnAction(e -> {
            handleCalculate(vendorIdField, lockInScoreField, migrationDifficultyField,
                          dataExportField, contractFlexibilityField, technicalComplexityField);
        });
        
        Button refreshButton = new Button("Refresh Rankings");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setPrefWidth(Double.MAX_VALUE);
        refreshButton.setOnAction(e -> refreshTable());
        
        panel.getChildren().addAll(sectionTitle, vendorIdField, lockInScoreField,
                                  migrationDifficultyField, dataExportField,
                                  contractFlexibilityField, technicalComplexityField,
                                  calculateButton, refreshButton);
        
        return panel;
    }
    
    private VBox createResultsPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("results-section");
        panel.setPadding(new Insets(30));
        
        Label sectionTitle = new Label("Vendor Exit Readiness Rankings");
        sectionTitle.getStyleClass().add("section-title");
        
        // Readiness table (sorted by AVL tree)
        readinessTable = createReadinessTable();
        
        panel.getChildren().addAll(sectionTitle, readinessTable);
        VBox.setVgrow(readinessTable, Priority.ALWAYS);
        
        return panel;
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("form-input");
        return field;
    }
    
    private TableView<VendorReadiness> createReadinessTable() {
        TableView<VendorReadiness> table = new TableView<>();
        table.getStyleClass().add("results-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Rank column
        javafx.scene.control.TableColumn<VendorReadiness, Integer> rankCol = new javafx.scene.control.TableColumn<>("#");
        rankCol.setCellValueFactory(data -> data.getValue().rankProperty().asObject());
        rankCol.setPrefWidth(50);
        
        // Vendor ID column
        javafx.scene.control.TableColumn<VendorReadiness, String> idCol = new javafx.scene.control.TableColumn<>("Vendor ID");
        idCol.setCellValueFactory(data -> data.getValue().vendorIdProperty());
        idCol.setPrefWidth(150);
        
        // Exit Readiness Score column with progress bar
        javafx.scene.control.TableColumn<VendorReadiness, Double> scoreCol = new javafx.scene.control.TableColumn<>("Exit Readiness");
        scoreCol.setCellValueFactory(data -> data.getValue().readinessScoreProperty().asObject());
        scoreCol.setCellFactory(col -> new javafx.scene.control.TableCell<VendorReadiness, Double>() {
            private final StackPane stackPane = new StackPane();
            private final javafx.scene.shape.Rectangle bar = new javafx.scene.shape.Rectangle();
            private final Label label = new Label();
            
            {
                bar.setHeight(20);
                bar.setArcWidth(10);
                bar.setArcHeight(10);
                stackPane.getChildren().addAll(bar, label);
                stackPane.setAlignment(Pos.CENTER_LEFT);
                stackPane.setPadding(new Insets(5));
            }
            
            @Override
            protected void updateItem(Double score, boolean empty) {
                super.updateItem(score, empty);
                if (empty || score == null) {
                    setGraphic(null);
                } else {
                    double width = (score / 100.0) * 250;
                    bar.setWidth(width);
                    
                    // Color: red (low readiness) -> yellow -> green (high readiness)
                    if (score < 33) {
                        bar.setFill(Color.rgb(244, 67, 54)); // Red
                    } else if (score < 66) {
                        bar.setFill(Color.rgb(255, 193, 7)); // Amber
                    } else {
                        bar.setFill(Color.rgb(76, 175, 80)); // Green
                    }
                    
                    DecimalFormat df = new DecimalFormat("#.##");
                    label.setText(df.format(score));
                    label.setTextFill(Color.WHITE);
                    
                    setGraphic(stackPane);
                }
            }
        });
        scoreCol.setPrefWidth(300);
        
        table.getColumns().addAll(rankCol, idCol, scoreCol);
        
        return table;
    }
    
    private void handleCalculate(TextField vendorId, TextField lockIn, TextField migration,
                                TextField dataExport, TextField contract, TextField technical) {
        try {
            if (vendorId.getText().isEmpty()) {
                showError("Vendor ID is required");
                return;
            }
            
            double lockInScore = Double.parseDouble(lockIn.getText());
            double migrationDifficulty = Double.parseDouble(migration.getText());
            int dataExportCap = Integer.parseInt(dataExport.getText());
            int contractFlex = Integer.parseInt(contract.getText());
            int techComplexity = Integer.parseInt(technical.getText());
            
            dashboard.addVendorMetrics(vendorId.getText(), lockInScore, migrationDifficulty,
                                      dataExportCap, contractFlex, techComplexity);
            
            double readiness = dashboard.calculateExitReadiness(vendorId.getText());
            
            DecimalFormat df = new DecimalFormat("#.##");
            showSuccess("Exit Readiness Score: " + df.format(readiness));
            
            refreshTable();
            
            // Clear form
            vendorId.clear();
            lockIn.clear();
            migration.clear();
            dataExport.clear();
            contract.clear();
            technical.clear();
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private void refreshTable() {
        readinessTable.getItems().clear();
        
        try {
            String[][] vendors = dashboard.getVendorsSortedByReadiness();
            int rank = 1;
            for (String[] entry : vendors) {
                if (entry.length >= 2) {
                    VendorReadiness vr = new VendorReadiness(rank++, entry[0],
                                                           Double.parseDouble(entry[1]));
                    readinessTable.getItems().add(vr);
                }
            }
        } catch (Exception e) {
            showError("Error loading readiness data: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return root;
    }
    
    // Data model
    public static class VendorReadiness {
        private javafx.beans.property.SimpleIntegerProperty rank;
        private javafx.beans.property.SimpleStringProperty vendorId;
        private javafx.beans.property.SimpleDoubleProperty readinessScore;
        
        public VendorReadiness(int rank, String id, double score) {
            this.rank = new javafx.beans.property.SimpleIntegerProperty(rank);
            this.vendorId = new javafx.beans.property.SimpleStringProperty(id);
            this.readinessScore = new javafx.beans.property.SimpleDoubleProperty(score);
        }
        
        public javafx.beans.property.IntegerProperty rankProperty() { return rank; }
        public javafx.beans.property.StringProperty vendorIdProperty() { return vendorId; }
        public javafx.beans.property.DoubleProperty readinessScoreProperty() { return readinessScore; }
    }
}

