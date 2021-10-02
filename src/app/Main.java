package app;

import app.base.DictionaryManagement;
import app.base.DictionaryV2;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
  Scene scene2, scene1;

  public static void main(String[] args) {
    DictionaryManagement.insertFromFile();
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader1 = new FXMLLoader();
    fxmlLoader1.setLocation(getClass().getResource("MainWindow.fxml"));
    FXMLLoader fxmlLoader2 = new FXMLLoader();
    fxmlLoader2.setLocation(getClass().getResource("translateWordAndSentence.fxml"));
    Parent root = fxmlLoader1.load();
    Parent subroot = fxmlLoader2.load();

    MainWindowController controllerMainWindow = fxmlLoader1.getController();
    Button buttonSwitchToTranslate = controllerMainWindow.getButtonSwitch();
    TranslateWordAndSentence controllerTranslate = fxmlLoader2.getController();
    Button buttonSwitchToMainWindow = controllerTranslate.getComeback();
    scene2 = new Scene(subroot, 800, 800);
    scene1 = new Scene(root, 800, 800);

    buttonSwitchToTranslate.setOnAction(
        event -> {
          primaryStage.setScene(scene2);
        });
    buttonSwitchToMainWindow.setOnAction(
        event -> {
          primaryStage.setScene(scene1);
        });
    primaryStage.setScene(scene1);
    primaryStage.setTitle("Dictionary");
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
