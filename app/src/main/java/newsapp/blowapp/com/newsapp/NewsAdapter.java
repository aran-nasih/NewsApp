package newsapp.blowapp.com.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aran on 11/1/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        News news = getItem(position);

        TextView newsTitle = listItemView.findViewById(R.id.news_title);
        TextView newsDate = listItemView.findViewById(R.id.news_date);
        TextView newsCategory = listItemView.findViewById(R.id.news_category);
        TextView newsAuthor = listItemView.findViewById(R.id.news_author);

        newsTitle.setText(news.getTitle());
        newsDate.setText(news.getDate());
        newsCategory.setText(news.getCategory());
        newsAuthor.setText(news.getAuthor());

        return listItemView;
    }
}
