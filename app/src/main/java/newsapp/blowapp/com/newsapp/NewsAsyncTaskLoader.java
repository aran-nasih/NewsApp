package newsapp.blowapp.com.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Aran on 11/1/2017.
 */

public class NewsAsyncTaskLoader extends AsyncTaskLoader<List<News>> {
    String url;

    public NewsAsyncTaskLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) return null;
        List<News> result = QueryUtils.fetchData(this.url);
        return result;
    }
}
