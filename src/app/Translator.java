package app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Translator {
  public static String oxfordSearch(String inputWord) {
    System.out.println("Loading...");
    String language = "en-gb";
    String word = inputWord;
    String fields = "definitions,pronunciations";
    String strictMatch = "false";
    String word_id = word.toLowerCase();
    String restUrl =
        "https://od-api.oxforddictionaries.com:443/api/v2/entries/"
            + language
            + "/"
            + word_id
            + "?"
            + "fields="
            + fields
            + "&strictMatch="
            + strictMatch;
    // TODO: replace with your own app id and app key
    String app_id = "c2e7b1fe";
    String app_key = "b41be5b3b08f91ebe3d849570f15c52a";
    try {
      URL url = new URL(restUrl);
      HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
      urlConnection.setRequestProperty("Accept", "application/json");
      urlConnection.setRequestProperty("app_id", app_id);
      urlConnection.setRequestProperty("app_key", app_key);

      // read the output from the server
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line + "\n");
      }
      //            System.out.println(stringBuilder);

      Object obj = JSONValue.parse(stringBuilder.toString());
      JSONObject jsonObject = (JSONObject) obj;
      JSONArray data = (JSONArray) jsonObject.get("results");
      JSONObject subJSON = (JSONObject) data.get(0);
      JSONArray data1 = (JSONArray) subJSON.get("lexicalEntries");
      JSONObject subJSON1 = (JSONObject) data1.get(0);
      JSONArray data2 = (JSONArray) subJSON1.get("entries");
      JSONObject subJSON2 = (JSONObject) data2.get(0);
      JSONArray data3 = (JSONArray) subJSON2.get("senses");
      JSONObject subJSON3 = (JSONObject) data3.get(0);
      JSONArray data4 = (JSONArray) subJSON3.get("definitions");
      Object res = data4.get(0);
      return "That word means: " + res + ".";
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "Can't find this word!";
  }
}
