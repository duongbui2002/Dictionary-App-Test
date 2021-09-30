package app;

import app.base.DictionaryManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

  public static void main(String[] args) {
    DictionaryManagement.insertFromFile();
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
    primaryStage.setTitle("Dictionary");
    primaryStage.setScene(new Scene(root, 900, 500));
    primaryStage.show();
  }
}