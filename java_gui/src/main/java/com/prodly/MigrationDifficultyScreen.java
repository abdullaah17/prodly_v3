package com.prodly;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Module 2: Migration Difficulty Analyzer Screen
 * Professional UI with task management and sequence visualization
 */
public class MigrationDifficultyScreen {
    private ProdlyApplication app;
    private VBox root;
    private MigrationDifficultyAnalyzerJNI analyzer;
    private TableView<MigrationTask> tasksTable;
    private ListView<String> sequenceList;
    private Label totalDaysLabel;
    private Label difficultyScoreLabel;
    private String currentVendorId = "";
    
    public MigrationDifficultyScreen(ProdlyApplication app) {
        this.app = app;
        this.analyzer = new MigrationDifficultyAnalyzerJNI();
        createUI();
    }
    
    private void createUI() {
        root = new VBox(30);
        root.setPadding(new Insets(40, 50, 40, 50));
        root.getStyleClass().add("screen-container");
        
        // Header
        VBox header = createHeader("Migration Difficulty Analyzer",
            "Analyze migration complexity and generate optimal task sequences");
        
        // Main content
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left: Task Form and List
        VBox leftPanel = createLeftPanel();
        leftPanel.setPrefWidth(500);
        
        // Right: Analysis Results
        VBox rightPanel = createRightPanel();
        VBox.setVgrow(rightPanel, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        
        root.getChildren().addAll(header, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        
        // Load sample data for default vendor after UI is created
        // Use Platform.runLater to ensure UI is fully initialized
        javafx.application.Platform.runLater(() -> {
            loadSampleDataForVendor("AWS-001");
        });
    }
    
    private void loadSampleDataForVendor(String vendorId) {
        // Load and display tasks for the sample vendor
        try {
            analyzer.setCurrentVendorId(vendorId);
            List<MigrationDifficultyAnalyzerJNI.TaskData> tasks = analyzer.getTasksForVendor(vendorId);
            
            if (tasks != null && !tasks.isEmpty()) {
                tasksTable.getItems().clear();
                for (MigrationDifficultyAnalyzerJNI.TaskData task : tasks) {
                    MigrationTask mt = new MigrationTask(task.taskId, task.taskName, 
                                                        task.difficulty, task.estimatedDays);
                    tasksTable.getItems().add(mt);
                }
            }
            
            if (vendorIdField != null) {
                vendorIdField.setText(vendorId);
            }
        } catch (Exception e) {
            System.err.println("Error loading sample data: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    private TextField vendorIdField;
    
    private VBox createLeftPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("form-section");
        panel.setPadding(new Insets(30));
        
        // Vendor ID input
        Label sectionTitle = new Label("Migration Task Management");
        sectionTitle.getStyleClass().add("section-title");
        
        vendorIdField = createTextField("Vendor ID (for this migration)");
        vendorIdField.setText("AWS-001"); // Pre-populate with sample vendor
        vendorIdField.setOnAction(e -> {
            // Load tasks when vendor ID changes
            String vendorId = vendorIdField.getText().isEmpty() ? "default" : vendorIdField.getText();
            loadSampleDataForVendor(vendorId);
        });
        
        // Task form
        VBox taskForm = new VBox(15);
        taskForm.getStyleClass().add("task-form");
        
        TextField taskIdField = createTextField("Task ID");
        TextField taskNameField = createTextField("Task Name");
        TextField difficultyField = createTextField("Difficulty (1-10)");
        TextField estimatedDaysField = createTextField("Estimated Days");
        TextArea dependenciesArea = new TextArea();
        dependenciesArea.setPromptText("Dependencies (one per line)");
        dependenciesArea.setPrefRowCount(3);
        dependenciesArea.getStyleClass().add("form-textarea");
        
        Button addTaskButton = new Button("Add Task");
        addTaskButton.getStyleClass().add("primary-button");
        addTaskButton.setPrefWidth(Double.MAX_VALUE);
        addTaskButton.setOnAction(e -> {
            handleAddTask(vendorIdField, taskIdField, taskNameField,
                         difficultyField, estimatedDaysField, dependenciesArea);
        });
        
        taskForm.getChildren().addAll(sectionTitle, vendorIdField, taskIdField,
                                     taskNameField, difficultyField, estimatedDaysField,
                                     dependenciesArea, addTaskButton);
        
        // Tasks table
        tasksTable = createTasksTable();
        tasksTable.setPrefHeight(200);
        
        // Calculate button
        Button calculateButton = new Button("Calculate Migration Difficulty");
        calculateButton.getStyleClass().add("primary-button");
        calculateButton.setPrefWidth(Double.MAX_VALUE);
        calculateButton.setOnAction(e -> {
            String vendorId = vendorIdField.getText().isEmpty() ? "default" : vendorIdField.getText();
            handleCalculate(vendorId);
            // Also reload tasks for this vendor
            loadSampleDataForVendor(vendorId);
        });
        
        panel.getChildren().addAll(taskForm, tasksTable, calculateButton);
        VBox.setVgrow(tasksTable, Priority.ALWAYS);
        
        return panel;
    }
    
    private VBox createRightPanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("results-section");
        panel.setPadding(new Insets(30));
        
        Label sectionTitle = new Label("Analysis Results");
        sectionTitle.getStyleClass().add("section-title");
        
        // Score display
        VBox scoreBox = new VBox(15);
        scoreBox.getStyleClass().add("score-display");
        scoreBox.setPadding(new Insets(20));
        
        difficultyScoreLabel = new Label("Difficulty Score: --");
        difficultyScoreLabel.getStyleClass().add("score-label");
        
        totalDaysLabel = new Label("Total Estimated Days: --");
        totalDaysLabel.getStyleClass().add("score-label");
        
        scoreBox.getChildren().addAll(difficultyScoreLabel, totalDaysLabel);
        
        // Optimal sequence
        Label sequenceTitle = new Label("Optimal Migration Sequence");
        sequenceTitle.getStyleClass().add("subsection-title");
        
        sequenceList = new ListView<>();
        sequenceList.getStyleClass().add("sequence-list");
        sequenceList.setPrefHeight(300);
        
        panel.getChildren().addAll(sectionTitle, scoreBox, sequenceTitle, sequenceList);
        VBox.setVgrow(sequenceList, Priority.ALWAYS);
        
        return panel;
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("form-input");
        return field;
    }
    
    private TableView<MigrationTask> createTasksTable() {
        TableView<MigrationTask> table = new TableView<>();
        table.getStyleClass().add("results-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<MigrationTask, String> idCol = new TableColumn<>("Task ID");
        idCol.setCellValueFactory(data -> data.getValue().taskIdProperty());
        
        TableColumn<MigrationTask, String> nameCol = new TableColumn<>("Task Name");
        nameCol.setCellValueFactory(data -> data.getValue().taskNameProperty());
        
        TableColumn<MigrationTask, Integer> difficultyCol = new TableColumn<>("Difficulty");
        difficultyCol.setCellValueFactory(data -> data.getValue().difficultyProperty().asObject());
        
        TableColumn<MigrationTask, Integer> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(data -> data.getValue().estimatedDaysProperty().asObject());
        
        table.getColumns().addAll(idCol, nameCol, difficultyCol, daysCol);
        
        return table;
    }
    
    private void handleAddTask(TextField vendorId, TextField taskId, TextField taskName,
                              TextField difficulty, TextField estimatedDays, TextArea dependencies) {
        try {
            if (taskId.getText().isEmpty() || taskName.getText().isEmpty()) {
                showError("Task ID and Name are required");
                return;
            }
            
            currentVendorId = vendorId.getText().isEmpty() ? "default" : vendorId.getText();
            int diff = Integer.parseInt(difficulty.getText());
            int days = Integer.parseInt(estimatedDays.getText());
            
            // Parse dependencies
            String[] depsArray = dependencies.getText().split("\n");
            List<String> depsList = new ArrayList<>();
            for (String dep : depsArray) {
                String trimmed = dep.trim();
                if (!trimmed.isEmpty()) {
                    depsList.add(trimmed);
                }
            }
            
            // Set vendor ID and add task
            analyzer.setCurrentVendorId(currentVendorId);
            analyzer.addTask(taskId.getText(), taskName.getText(), diff, days,
                           depsList.toArray(new String[0]));
            
            // Add to table
            MigrationTask task = new MigrationTask(taskId.getText(), taskName.getText(),
                                                  diff, days);
            tasksTable.getItems().add(task);
            
            // Clear form
            taskId.clear();
            taskName.clear();
            difficulty.clear();
            estimatedDays.clear();
            dependencies.clear();
            
            showSuccess("Task added successfully");
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private void handleCalculate(String vendorId) {
        try {
            if (vendorId.isEmpty()) {
                vendorId = "default";
            }
            
            double difficulty = analyzer.calculateMigrationDifficulty(vendorId);
            int totalDays = analyzer.getTotalMigrationDays(vendorId);
            String[] sequence = analyzer.getOptimalMigrationSequence(vendorId);
            
            DecimalFormat df = new DecimalFormat("#.##");
            difficultyScoreLabel.setText("Difficulty Score: " + df.format(difficulty) + " / 100");
            totalDaysLabel.setText("Total Estimated Days: " + totalDays);
            
            sequenceList.getItems().clear();
            for (int i = 0; i < sequence.length; i++) {
                sequenceList.getItems().add((i + 1) + ". " + sequence[i]);
            }
            
        } catch (Exception e) {
            showError("Error calculating migration difficulty: " + e.getMessage());
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
    public static class MigrationTask {
        private javafx.beans.property.SimpleStringProperty taskId;
        private javafx.beans.property.SimpleStringProperty taskName;
        private javafx.beans.property.SimpleIntegerProperty difficulty;
        private javafx.beans.property.SimpleIntegerProperty estimatedDays;
        
        public MigrationTask(String id, String name, int diff, int days) {
            this.taskId = new javafx.beans.property.SimpleStringProperty(id);
            this.taskName = new javafx.beans.property.SimpleStringProperty(name);
            this.difficulty = new javafx.beans.property.SimpleIntegerProperty(diff);
            this.estimatedDays = new javafx.beans.property.SimpleIntegerProperty(days);
        }
        
        public javafx.beans.property.StringProperty taskIdProperty() { return taskId; }
        public javafx.beans.property.StringProperty taskNameProperty() { return taskName; }
        public javafx.beans.property.IntegerProperty difficultyProperty() { return difficulty; }
        public javafx.beans.property.IntegerProperty estimatedDaysProperty() { return estimatedDays; }
    }
}

