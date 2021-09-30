package app;

import app.base.Dictionary;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainWindowController {
  private ObservableList<String> data =
      FXCollections.observableArrayList(Dictionary.getInstance().getWordTarget());
  @FXML private ListView<String> listView;

  @FXML private TextArea itemDetailsTextArea;
  @FXML private TextField textSearch;

  public void initialize() {

    listView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                  String word = listView.getSelectionModel().getSelectedItem();
                  String wordExplain =
                      Dictionary.getDictionary()
                          .get(
                              Dictionary.getInstance()
                                  .binaryLookUp(word, 0, Dictionary.getSize() - 1))
                          .getWord_explain();
                  itemDetailsTextArea.setText(wordExplain);
                }
              }
            });
    FilteredList<String> flWordTarget = new FilteredList<>(data, p -> true);

    textSearch.setPromptText("Search here");
    textSearch
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              flWordTarget.setPredicate(
                  p -> p.toLowerCase().contains(newValue.toLowerCase().trim()));
            }));
    listView.setItems(flWordTarget);
    listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    listView.getSelectionModel().selectFirst();
  }

  @FXML
  public void handleClickListView() {
    String item = listView.getSelectionModel().getSelectedItem();
    itemDetailsTextArea.setText(item);
  }
}
