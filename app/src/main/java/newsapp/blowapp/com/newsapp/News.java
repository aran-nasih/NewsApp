package newsapp.blowapp.com.newsapp;

/**
 * Created by Aran on 11/1/2017.
 */

public class News {
    private String title;
    private String date;
    private String category;
    private String author;
    private String url;

    public News(String title, String date, String category, String author, String url) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
