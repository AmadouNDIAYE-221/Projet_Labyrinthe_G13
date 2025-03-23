package labyrinthe_package;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos; 


public class LabyrintheUI {

    private Labyrinthe labyrinthe;
    private Labyrinthe previousLabyrinthe;
    private final Canvas canvas;
    private Label dfsResultLabel, bfsResultLabel, dfsStepsLabel, bfsStepsLabel;

    public LabyrintheUI(Stage primaryStage) {
        this.labyrinthe = new Labyrinthe(20);
        this.canvas = new Canvas(600, 600);

        Button genererButton = new Button("Générer");
        Button retourButton = new Button("Retour");
        Button dfsButton = new Button("Résolution DFS");
        Button bfsButton = new Button("Résolution BFS");
        Button effacerButton = new Button("Effacer");
        Button quitterButton = new Button("Quitter");

        dfsResultLabel = new Label();
        bfsResultLabel = new Label();
        dfsStepsLabel = new Label();
        bfsStepsLabel = new Label();

        genererButton.setOnAction(e -> {
            previousLabyrinthe = labyrinthe.clone();
            labyrinthe = new Labyrinthe(20);
            labyrinthe.genererLabyrinthe();
            labyrinthe.effacerChemin();
            drawLabyrinthe();
        });

        retourButton.setOnAction(e -> {
            if (previousLabyrinthe != null) {
                labyrinthe = previousLabyrinthe.clone();
                drawLabyrinthe();
                clearLabels();
            }
        });

        
        dfsButton.setOnAction(e -> {
            long startTime = System.nanoTime();
            int steps = labyrinthe.resoudreDFS();
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            dfsResultLabel.setText("DFS: " + (steps > 0 ? "Résolu" : "Pas de solution") + ", Temps: " + duration + " ms");
            dfsStepsLabel.setText("Étapes: " + steps);
            drawLabyrinthe();
        });

        bfsButton.setOnAction(e -> {
            long startTime = System.nanoTime();
            int steps = labyrinthe.resoudreBFS();
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            bfsResultLabel.setText("BFS: " + (steps > 0 ? "Résolu" : "Pas de solution") + ", Temps: " + duration + " ms");
            bfsStepsLabel.setText("Étapes: " + steps);
            drawLabyrinthe();
        });

        effacerButton.setOnAction(e -> {
            labyrinthe.effacerChemin();
            drawLabyrinthe();
            clearLabels();
        });

        quitterButton.setOnAction(e -> primaryStage.close());


        VBox buttonsAndLabels = new VBox(10, genererButton, retourButton, dfsButton, dfsResultLabel, dfsStepsLabel, bfsButton, bfsResultLabel, bfsStepsLabel, effacerButton, quitterButton);

        buttonsAndLabels.setPrefWidth(150); 
        buttonsAndLabels.setPrefHeight(600); 
        buttonsAndLabels.setPadding(new javafx.geometry.Insets(70, 0, 0, -30)); 
        buttonsAndLabels.setAlignment(Pos.TOP_LEFT);  // Alignement buttons top left

    
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setRight(buttonsAndLabels);

        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Résolution de Labyrinthe");
        primaryStage.show();

        drawLabyrinthe();
    }

    private void clearLabels() {
        dfsResultLabel.setText("");
        bfsResultLabel.setText("");
        dfsStepsLabel.setText("");
        bfsStepsLabel.setText("");
    }

    private void drawLabyrinthe() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int rows = labyrinthe.getTaille();
        int cols = labyrinthe.getTaille();
        double cellWidth = canvas.getWidth() / cols;
        double cellHeight = canvas.getHeight() / rows;

        int[][] grille = labyrinthe.getGrille();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grille[i][j] == 1) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                gc.setStroke(Color.GRAY);
                gc.strokeRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }

        drawStartAndEnd(gc, cellWidth, cellHeight);
        drawPath(gc, cellWidth, cellHeight);
    }

    private void drawStartAndEnd(GraphicsContext gc, double cellWidth, double cellHeight) {
        
        gc.setFill(Color.GREEN);
        gc.fillRect(labyrinthe.getDepartY() * cellWidth, labyrinthe.getDepartX() * cellHeight, cellWidth, cellHeight);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText("E", labyrinthe.getDepartY() * cellWidth + cellWidth / 3, labyrinthe.getDepartX() * cellHeight + cellHeight / 1.5);

        gc.setFill(Color.RED);
        gc.fillRect(labyrinthe.getArriveeY() * cellWidth, labyrinthe.getArriveeX() * cellHeight, cellWidth, cellHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("S", labyrinthe.getArriveeY() * cellWidth + cellWidth / 3, labyrinthe.getArriveeX() * cellHeight + cellHeight / 1.5);
    }

    private void drawPath(GraphicsContext gc, double cellWidth, double cellHeight) {
        // Draw the path
        if (labyrinthe.getChemin() != null) {
            gc.setFill(Color.BLUE);
            for (Point p : labyrinthe.getChemin()) {
                if (!(p.x == labyrinthe.getArriveeX() && p.y == labyrinthe.getArriveeY())
                        && !(p.x == labyrinthe.getDepartX() && p.y == labyrinthe.getDepartY())) {
                    gc.fillText("+", p.y * cellWidth + cellWidth / 3, p.x * cellHeight + cellHeight / 1.5);
                }
            }
        }
    }
}
