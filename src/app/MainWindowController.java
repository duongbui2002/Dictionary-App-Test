package app;

import app.base.Dictionary;
import app.base.DictionaryV2;
import app.base.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class MainWindowController {
  private ObservableList<Word> data =
      FXCollections.observableArrayList(DictionaryV2.getDictionary());
  @FXML private ListView<Word> listView;
  @FXML private TextArea itemDetailsTextArea;
  @FXML private TextField textSearch;
  @FXML private BorderPane mainBorderPane;
  @FXML private ContextMenu listContextMenu;
  @FXML private Button buttonSwitch;

  public void initialize() {
    listContextMenu = new ContextMenu();
    MenuItem deleteWordMenu = new MenuItem("Delete");
    deleteWordMenu.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            Word word_to_delete = listView.getSelectionModel().getSelectedItem();
            deleteWord(word_to_delete);
          }
        });
    listContextMenu.getItems().addAll(deleteWordMenu);
    listView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<Word>() {
              @Override
              public void changed(
                  ObservableValue<? extends Word> observable, Word oldValue, Word newValue) {
                if (newValue != null) {
                  Word word = listView.getSelectionModel().getSelectedItem();
                  itemDetailsTextArea.setText(word.getWord_explain());
                }
              }
            });
    FilteredList<Word> flWordTarget = new FilteredList<>(data, p -> true);

    textSearch.setPromptText("Search here");
    textSearch
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              flWordTarget.setPredicate(
                  p -> p.getWord_target().toLowerCase().contains(newValue.toLowerCase().trim()));
            }));
    if (flWordTarget.size() > 0) {
      listView.setItems(flWordTarget);
    } else {
      listView.getItems().setAll();
    }

    listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    listView.getSelectionModel().selectFirst();

    listView.setCellFactory(
        new Callback<ListView<Word>, ListCell<Word>>() {
          @Override
          public ListCell<Word> call(ListView<Word> param) {
            ListCell<Word> cell =
                new ListCell<Word>() {

                  @Override
                  protected void updateItem(Word item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                      setText(null);
                    } else {
                      setText(item.getWord_target());
                    }
                  }
                };

            cell.emptyProperty()
                .addListener(
                    (obs, wasEmpty, isNowEmpty) -> {
                      if (isNowEmpty) {
                        cell.setContextMenu(null);
                      } else {
                        cell.setContextMenu(listContextMenu);
                      }
                    });

            return cell;
          }
        });
  }

  @FXML
  public void showNewWordDialog() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(mainBorderPane.getScene().getWindow());
    dialog.setTitle("Add New Word");
    dialog.setHeaderText("Use this dialog to add a new Word");
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("Dialog.fxml"));
    try {
      dialog.getDialogPane().setContent(fxmlLoader.load());

    } catch (IOException e) {
      System.out.println("Couldn't load the dialog");
      e.printStackTrace();
      return;
    }

    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    Optional<ButtonType> result = dialog.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      DialogController controller = fxmlLoader.getController();

      Word newWord = controller.processResults();
      if (newWord != null) {
        listView.setItems(DictionaryV2.getDictionary());
        listView.getSelectionModel().select(newWord);
      } else {
        showNewWordDialog();
      }
    }
  }

  @FXML
  public void handleClickListView() {
    Word item = listView.getSelectionModel().getSelectedItem();
    itemDetailsTextArea.setText(item.getWord_explain());
  }

  public void deleteWord(Word word_to_delete) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete this word");
    alert.setHeaderText("Delete this word: " + word_to_delete.getWord_target());
    alert.setContentText("Are you sure?  Press OK to confirm, or cancel to Back out.");
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && (result.get() == ButtonType.OK)) {
      DictionaryV2.getDictionary().remove(word_to_delete);
    }
  }

  @FXML
  public void handleKeyPressed(KeyEvent keyEvent) {
    Word selectedWord = listView.getSelectionModel().getSelectedItem();
    if (selectedWord != null) {
      if (keyEvent.getCode().equals(KeyCode.DELETE)) {
        deleteWord(selectedWord);
      }
    }
  }

  public Button getButtonSwitch() {
    return buttonSwitch;
  }
}
