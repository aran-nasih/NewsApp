package newsapp.blowapp.com.newsapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aran on 11/1/2017.
 */

public class QueryUtils {
    public static List<News> fetchData(String urlString) {
        URL url = generateUrl(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println("FAILED MAKING HTTP REQUEST");
        }
        List<News> news = parseJson(jsonResponse);
        return news;
    }

    public static URL generateUrl(String urlString) {
        if (urlString == null || urlString.length() < 1) return null;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.out.println("FAILED GENERATING URL");
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) return jsonResponse;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("BAD RESPONSE CODE");
            }
        } catch (IOException e) {
            System.out.println("FAILED OPENING CONNECTION");
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> parseJson(String json) {
        List<News> books = new ArrayList<>();
        JSONObject jsonBase;
        try {
            jsonBase = new JSONObject(json);
            JSONObject response = jsonBase.getJSONObject("response");
            JSONArray newsResults = response.getJSONArray("results");
            for (int i = 0; i < newsResults.length(); i++) {
                String title = "title not available";
                String author = "by unknown";
                String date = "unknown date";
                String category = "no category";
                String link = "";

                JSONObject currentItem;
                currentItem = newsResults.getJSONObject(i);

                if (currentItem.has("webTitle"))
                    title = currentItem.optString("webTitle");
                if (currentItem.has("sectionName"))
                    category = currentItem.optString("sectionName");
                if (currentItem.has("webPublicationDate"))
                    date = currentItem.optString("webPublicationDate");
                if (currentItem.has("webUrl"))
                    link = currentItem.optString("webUrl");
                if (currentItem.has("author"))
                    author = currentItem.optString("author");

                News book = new News(title, date, category, author, link);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}

