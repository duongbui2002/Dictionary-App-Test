package app.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class DictionaryV2 {
  private static DictionaryV2 instance = new DictionaryV2();
  private static String filename = "ListWord.txt";

  private static ObservableList<Word> dictionary;

  public ObservableList<Word> getWordsList() {
    return dictionary;
  }

  public static DictionaryV2 getInstance() {
    return instance;
  }

  public static int getSize() {
    return dictionary.size();
  }

  private static int searchIndexToInsert(int st, int end, Word word_add) {
    if (end < st) return st;
    int mid = st + (end - st) / 2;
    if (mid == dictionary.size()) return mid;
    Word word = dictionary.get(mid);
    int compare = word.compareTo(word_add);
    if (compare == 0) return -1; // If -1, this word already exists
    if (compare > 0) return searchIndexToInsert(st, mid - 1, word_add);
    return searchIndexToInsert(mid + 1, end, word_add);
  }

  public static void sortDir() {
    Collections.sort(
        dictionary,
        new Comparator<Word>() {
          @Override
          public int compare(Word o1, Word o2) {
            return o1.getWord_target().compareTo(o2.getWord_target());
          }
        });
  }

  public Word binarySearch(String wordToSearch, int l, int r) {
    if (r < l) return null;
    int mid = l + (r - l) / 2;
    Word word = dictionary.get(mid);
    String current = word.getWord_target();
    int compare = current.compareTo(wordToSearch);
    if (compare == 0) return word;
    if (compare > 0) return binarySearch(wordToSearch, l, mid - 1);
    return binarySearch(wordToSearch, mid + 1, r);
  }

  public int binaryLookUp(String wordToSearch, int l, int r) {
    if (r < l) return -1;
    int mid = l + (r - l) / 2;
    Word word = dictionary.get(mid);
    String current = word.getWord_target();
    int compare = current.compareTo(wordToSearch);
    if (compare == 0) return mid;
    if (compare > 0) return binaryLookUp(wordToSearch, l, mid - 1);
    return binaryLookUp(wordToSearch, mid + 1, r);
  }

  public ArrayList<String> getWordTarget() {
    ArrayList<String> listWordTarget = new ArrayList<>();
    for (int i = 0; i < Dictionary.getSize(); i++) {
      listWordTarget.add(dictionary.get(i).getWord_target());
    }
    return listWordTarget;
  }

  public void LoadDictionary() throws IOException {
    dictionary = FXCollections.observableArrayList();
    Path path = Paths.get(filename);
    BufferedReader br = Files.newBufferedReader(path);
    try {
      String line = null;
      while ((line = br.readLine()) != null) {
        line = line.trim().toLowerCase();
        String[] separated_line = line.split("\t");
        dictionary.add(new Word(separated_line[0], separated_line[1]));
      }
      DictionaryV2.sortDir();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }

  public static ObservableList<Word> getDictionary() {
    return dictionary;
  }

  public void storeTodoItems() throws IOException {

    Path path = Paths.get(filename);
    BufferedWriter bw = Files.newBufferedWriter(path);
    try {
      Iterator<Word> iter = dictionary.iterator();
      while (iter.hasNext()) {
        Word word = iter.next();
        bw.write(String.format("%s\t%s", word.getWord_target(), word.getWord_explain()));
        bw.newLine();
      }
    } finally {
      if (bw != null) {
        bw.close();
      }
    }
  }

  public static void push(Word word) {
    int indexToAdd = searchIndexToInsert(0, dictionary.size() - 1, word);
    if (indexToAdd >= 0 && indexToAdd <= dictionary.size()) {
      dictionary.add(indexToAdd, word);
    }
  }
}
