package app.base;

import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryCommandline {
  public static void showAllWords() {
    StringBuilder result = new StringBuilder();
    result.append("NO   | English        | Vietnamese").append("\n");

    int maxSpaceBetween = 20;

    for (int i = 0; i < Dictionary.getSize(); i++) {
      result.append(i + 1).append("    | ");

      result.append(Dictionary.getDictionary().get(i).getWord_target());
      int currentLength = Dictionary.getDictionary().get(i).getWord_target().length();
      for (int j = currentLength; j < maxSpaceBetween; j++) {
        result.append(" ");
      }
      result.append(" | ");

      result.append(Dictionary.getDictionary().get(i).getWord_explain());
      result.append("\n");
    }
    System.out.println(result);
  }

  public static void dictionaryBasic() {
    DictionaryManagement.insertFromFile();
    showAllWords();
  }

  public static void dictionaryAdvanced() {
    DictionaryManagement.insertFromFile();
    DictionaryCommandline.showAllWords();
    DictionaryManagement.dictionaryLookup();
  }

  public static void dictionarySearcher() {
    ArrayList<Word> prefixList = new ArrayList<>();
    Scanner scanner_ = new Scanner(System.in);
    System.out.println("Enter the word that you want to search: ");
    String incompleteWord = scanner_.nextLine();
    for (int i = 0; i < Dictionary.getSize(); i++) {
      if (Dictionary.getDictionary().get(i).getWord_target().startsWith(incompleteWord)) {
        prefixList.add(Dictionary.getDictionary().get(i));
      }
    }
    if (!prefixList.isEmpty()) {
      for (int i = 0; i < prefixList.size(); i++) {
        System.out.println(prefixList.get(i).getWord_target());
      }
    } else {
      System.out.println("No search results");
    }
  }
}
