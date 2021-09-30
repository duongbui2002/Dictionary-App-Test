package app;

import app.base.DictionaryManagement;
import app.base.DictionaryV2;
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

  @Override
  public void init() throws Exception {
    try {
      DictionaryV2.getInstance().LoadDictionary();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void stop() throws Exception {
    try {
      DictionaryV2.getInstance().storeTodoItems();

    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
