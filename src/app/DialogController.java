package app;

import app.base.DictionaryV2;
import app.base.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;

import java.util.Locale;
import java.util.Optional;

public class DialogController {
  @FXML private TextField wordTarget;
  @FXML private TextArea explainArea;

  public Word processResults() {

    String word_target = wordTarget.getText().trim().toLowerCase();
    String word_explain = explainArea.getText().trim().toLowerCase();
    Word newWord = null;
    if (word_explain.isEmpty() || word_target.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Add word failed");
      alert.setContentText("Please enter the full word.");
      Optional<ButtonType> result_ = alert.showAndWait();
    } else {
      if (DictionaryV2.getInstance().binaryLookUp(word_target, 0, DictionaryV2.getSize() - 1)
          == -1) {
        newWord = new Word(word_target, word_explain);
        DictionaryV2.push(newWord);
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Add word failed");
        alert.setContentText("This word existed");
        Optional<ButtonType> result_ = alert.showAndWait();
      }
    }
    return newWord;
  }

  public void typedInputTarget(KeyEvent keyEvent) {}

  public void typedInputExplain(KeyEvent keyEvent) {}
}
