package app.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DictionaryManagement {
  public static Scanner scanner = new Scanner(System.in);

  public static void insertFromCommandLine() {
    boolean t = false;
    int numberOfElem = 0;
    // To enter the number of elements
    do {
      System.out.println("Enter the number of words you want to add: ");
      if (scanner.hasNextInt()) {
        numberOfElem = scanner.nextInt();
        if (numberOfElem > 0) {
          t = true;
        } else {
          t = false;
          System.out.println("Input error, re-enter: ");
        }
      } else {
        System.out.println("Input error, re-enter: ");
        scanner.nextLine();
      }
    } while (!t);
    scanner.nextLine();
    for (int i = 0; i < numberOfElem; i++) {
      System.out.println((i + 1) + ": ");
      System.out.println("In English: ");
      String word_target = scanner.nextLine();
      System.out.println("In Vietnamese: ");
      String word_explain = scanner.nextLine();
      Dictionary.push(new Word(word_target, word_explain));
    }
  }

  public static void insertFromFile() {
    try {
      FileReader fileReader = new FileReader(new File("src/app/fileDictionary/dictionary.txt"));
      BufferedReader reader = new BufferedReader(fileReader);

      String line = null;
      while ((line = reader.readLine()) != null) {
        line = line.trim().toLowerCase();
        String[] separated_line = line.split("\t");
        Dictionary.getDictionary().add(new Word(separated_line[0], separated_line[1]));
      }
      Dictionary.sortDir();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void dictionaryLookup() {
    Scanner scanner_ = new Scanner(System.in);
    System.out.println("What is the word that you want to look up?");
    String wordLookUp = scanner_.nextLine();
    wordLookUp.trim().toLowerCase();
    Dictionary usingForBinarySearch = new Dictionary();
    Word hasWord = usingForBinarySearch.binarySearch(wordLookUp, 0, Dictionary.getSize() - 1);
    if (hasWord == null) {
      System.out.println("This word is not in the dictionary");
    } else {
      System.out.println(hasWord.getWord_target() + " means: " + hasWord.getWord_explain());
    }
  }

  public static void removeWord() {
    Scanner scanner_ = new Scanner(System.in);
    System.out.println("Enter the word you want to delete: ");
    String word_target = scanner.nextLine();
    Dictionary usingForBinarySearch = new Dictionary();
    int indexToDelete = usingForBinarySearch.binaryLookUp(word_target, 0, Dictionary.getSize() - 1);
    if (indexToDelete == -1) {
      System.out.println("This word is not in the dictionary");
    } else {
      Dictionary.getDictionary().remove(indexToDelete);
      System.out.println(word_target + "was removed.");
      Dictionary.sortDir();
      DictionaryCommandline.showAllWords();
    }
  }

  public static void correctWord() {
    Scanner scanner_ = new Scanner(System.in);
    System.out.println("Enter the word you want to correct: ");
    String word_target = scanner.nextLine();
    Dictionary usingForBinarySearch = new Dictionary();
    int indexToCorrect =
        usingForBinarySearch.binaryLookUp(word_target, 0, Dictionary.getSize() - 1);
    if (indexToCorrect == -1) {
      System.out.println("This word is not in the dictionary");
    } else {
      System.out.println("Enter the new word target: ");
      String newWordtarget = scanner_.nextLine();
      System.out.println("Enter the new word explain: ");
      String newWordExplain = scanner_.nextLine();
      Dictionary.getDictionary().get(indexToCorrect).setWord_target(newWordtarget);
      Dictionary.getDictionary().get(indexToCorrect).setWord_explain(newWordExplain);
      System.out.println(word_target + " was corrected.");
      Dictionary.sortDir();
      DictionaryCommandline.showAllWords();
    }
  }
}
