package labyrinthe_package;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        LabyrintheUI labyrintheUI = new LabyrintheUI(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
