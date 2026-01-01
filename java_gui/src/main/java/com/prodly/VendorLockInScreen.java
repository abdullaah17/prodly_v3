package com.prodly;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import java.text.DecimalFormat;

/**
 * Module 1: Vendor Lock-In Score Calculator Screen
 * Professional UI with modern design
 */
public class VendorLockInScreen {
    private ProdlyApplication app;
    private VBox root;
    private VendorLockInAnalyzerJNI analyzer;
    private TableView<VendorScore> resultsTable;
    
    public VendorLockInScreen(ProdlyApplication app) {
        this.app = app;
        this.analyzer = new VendorLockInAnalyzerJNI();
        createUI();
    }
    
    private void createUI() {
        root = new VBox(30);
        root.setPadding(new Insets(40, 50, 40, 50));
        root.getStyleClass().add("screen-container");
        
        // Header
        VBox header = createHeader("Vendor Lock-In Score Calculator", 
            "Analyze vendor lock-in risk based on contract terms, data volume, and dependencies");
        
        // Main content area with two columns
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left: Input Form
        VBox formContainer = createFormSection();
        formContainer.setPrefWidth(450);
        
        // Right: Results Table and Visualization
        VBox resultsContainer = createResultsSection();
        VBox.setVgrow(resultsContainer, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(formContainer, resultsContainer);
        HBox.setHgrow(resultsContainer, Priority.ALWAYS);
        
        root.getChildren().addAll(header, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        
        // Load initial data
        refreshResults();
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
    
    private VBox createFormSection() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-section");
        form.setPadding(new Insets(30));
        
        Label sectionTitle = new Label("Add Vendor");
        sectionTitle.getStyleClass().add("section-title");
        
        // Form fields
        TextField vendorIdField = createTextField("Vendor ID");
        TextField vendorNameField = createTextField("Vendor Name");
        TextField contractValueField = createTextField("Contract Value ($)");
        TextField contractMonthsField = createTextField("Contract Duration (months)");
        TextField dataVolumeField = createTextField("Data Volume (GB)");
        TextField apiDependenciesField = createTextField("API Dependencies");
        TextField switchingCostField = createTextField("Switching Cost ($)");
        
        CheckBox customIntegrationCheck = new CheckBox("Has Custom Integration");
        customIntegrationCheck.getStyleClass().add("form-checkbox");
        
        // Submit button
        Button submitButton = new Button("Calculate Lock-In Score");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setPrefWidth(Double.MAX_VALUE);
        submitButton.setOnAction(e -> {
            handleSubmit(vendorIdField, vendorNameField, contractValueField,
                        contractMonthsField, dataVolumeField, apiDependenciesField,
                        switchingCostField, customIntegrationCheck);
        });
        
        form.getChildren().addAll(sectionTitle, vendorIdField, vendorNameField,
                                 contractValueField, contractMonthsField,
                                 dataVolumeField, apiDependenciesField,
                                 switchingCostField, customIntegrationCheck, submitButton);
        
        return form;
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("form-input");
        return field;
    }
    
    private VBox createResultsSection() {
        VBox results = new VBox(20);
        results.getStyleClass().add("results-section");
        results.setPadding(new Insets(30));
        
        Label sectionTitle = new Label("Vendor Lock-In Scores");
        sectionTitle.getStyleClass().add("section-title");
        
        // Results table
        resultsTable = createResultsTable();
        
        // Action buttons
        HBox actionBar = new HBox(15);
        actionBar.setAlignment(Pos.CENTER_LEFT);
        
        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(e -> refreshResults());
        
        Button exportButton = new Button("Export Results");
        exportButton.getStyleClass().add("secondary-button");
        
        actionBar.getChildren().addAll(refreshButton, exportButton);
        
        results.getChildren().addAll(sectionTitle, resultsTable, actionBar);
        VBox.setVgrow(resultsTable, Priority.ALWAYS);
        
        return results;
    }
    
    private TableView<VendorScore> createResultsTable() {
        TableView<VendorScore> table = new TableView<>();
        table.getStyleClass().add("results-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Vendor ID column
        TableColumn<VendorScore, String> idCol = new TableColumn<>("Vendor ID");
        idCol.setCellValueFactory(data -> data.getValue().vendorIdProperty());
        idCol.setPrefWidth(150);
        
        // Vendor Name column
        TableColumn<VendorScore, String> nameCol = new TableColumn<>("Vendor Name");
        nameCol.setCellValueFactory(data -> data.getValue().vendorNameProperty());
        nameCol.setPrefWidth(200);
        
        // Lock-In Score column with colored bars
        TableColumn<VendorScore, Double> scoreCol = new TableColumn<>("Lock-In Score");
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty().asObject());
        scoreCol.setCellFactory(new Callback<TableColumn<VendorScore, Double>, TableCell<VendorScore, Double>>() {
            @Override
            public TableCell<VendorScore, Double> call(TableColumn<VendorScore, Double> param) {
                return new TableCell<VendorScore, Double>() {
                    private final StackPane stackPane = new StackPane();
                    private final Rectangle bar = new Rectangle();
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
                            double width = (score / 100.0) * 200;
                            bar.setWidth(width);
                            
                            // Color based on score: green (low) -> yellow -> red (high)
                            if (score < 33) {
                                bar.setFill(Color.rgb(76, 175, 80)); // Green
                            } else if (score < 66) {
                                bar.setFill(Color.rgb(255, 193, 7)); // Amber
                            } else {
                                bar.setFill(Color.rgb(244, 67, 54)); // Red
                            }
                            
                            DecimalFormat df = new DecimalFormat("#.##");
                            label.setText(df.format(score));
                            label.setTextFill(Color.WHITE);
                            
                            setGraphic(stackPane);
                        }
                    }
                };
            }
        });
        scoreCol.setPrefWidth(250);
        
        table.getColumns().addAll(idCol, nameCol, scoreCol);
        
        return table;
    }
    
    private void handleSubmit(TextField vendorId, TextField vendorName, TextField contractValue,
                             TextField contractMonths, TextField dataVolume, TextField apiDeps,
                             TextField switchingCost, CheckBox customIntegration) {
        try {
            // Validation
            if (vendorId.getText().isEmpty() || vendorName.getText().isEmpty()) {
                showError("Vendor ID and Name are required");
                return;
            }
            
            double contractVal = Double.parseDouble(contractValue.getText());
            int months = Integer.parseInt(contractMonths.getText());
            double dataVol = Double.parseDouble(dataVolume.getText());
            int apiDepsCount = Integer.parseInt(apiDeps.getText());
            double switchingCostVal = Double.parseDouble(switchingCost.getText());
            
            // Add vendor to analyzer
            analyzer.addVendor(vendorId.getText(), vendorName.getText(),
                              contractVal, months, dataVol, apiDepsCount,
                              customIntegration.isSelected(), switchingCostVal);
            
            // Calculate score
            double score = analyzer.calculateLockInScore(vendorId.getText());
            
            // Show success message
            showSuccess("Vendor added successfully. Lock-In Score: " + String.format("%.2f", score));
            
            // Refresh table
            refreshResults();
            
            // Clear form
            vendorId.clear();
            vendorName.clear();
            contractValue.clear();
            contractMonths.clear();
            dataVolume.clear();
            apiDeps.clear();
            switchingCost.clear();
            customIntegration.setSelected(false);
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private void refreshResults() {
        resultsTable.getItems().clear();
        
        try {
            String[][] scores = analyzer.getAllScores();
            if (scores != null && scores.length > 0) {
                for (String[] entry : scores) {
                    if (entry != null && entry.length >= 2) {
                        try {
                            String vendorId = entry[0];
                            String vendorName = (entry.length >= 3 && entry[2] != null && !entry[2].isEmpty()) 
                                ? entry[2] 
                                : ("Vendor " + vendorId); // Use vendor name if available, otherwise generate
                            double score = Double.parseDouble(entry[1]);
                            VendorScore vs = new VendorScore(vendorId, vendorName, score);
                            resultsTable.getItems().add(vs);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing score for vendor: " + entry[0]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading results: " + e.getMessage());
            e.printStackTrace();
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
    
    // Data model for table
    public static class VendorScore {
        private javafx.beans.property.SimpleStringProperty vendorId;
        private javafx.beans.property.SimpleStringProperty vendorName;
        private javafx.beans.property.SimpleDoubleProperty score;
        
        public VendorScore(String id, String name, double score) {
            this.vendorId = new javafx.beans.property.SimpleStringProperty(id);
            this.vendorName = new javafx.beans.property.SimpleStringProperty(name);
            this.score = new javafx.beans.property.SimpleDoubleProperty(score);
        }
        
        public javafx.beans.property.StringProperty vendorIdProperty() { return vendorId; }
        public javafx.beans.property.StringProperty vendorNameProperty() { return vendorName; }
        public javafx.beans.property.DoubleProperty scoreProperty() { return score; }
    }
}

