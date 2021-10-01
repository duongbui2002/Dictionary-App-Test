package app;

import app.base.Dictionary;
import app.base.DictionaryV2;
import app.base.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class MainWindowController {
  private ObservableList<Word> data =
      FXCollections.observableArrayList(DictionaryV2.getDictionary());
  @FXML private ListView<Word> listView;
  @FXML private TextArea itemDetailsTextArea;
  @FXML private TextField textSearch;
  @FXML private BorderPane mainBorderPane;

  public void initialize() {

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
}
