package nanodegree.rahall.popularmovies1;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import nanodegree.rahall.popularmovies1.models.MovieDetail;
import nanodegree.rahall.popularmovies1.models.Movies;

/**
 * Created by rahall on 8/30/15.
 */
public class MovieApplication  extends Application{
    private static MovieApplication mInstance;
    private static Context mContext;
    private static Movies mMovies;
    private static MovieDetail mMovieDetail;


    private RequestQueue mRequestQueue;

    public static final String SHARED_PREFERENCES = "movieappprefs";
    public static final String SORT_TYPE = "sortType";
    public static final String TAG = MovieApplication.class.getSimpleName();

    public static final String REQUEST_TYPE = "requestType";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();


    }

    public static Context getAppContext() {
        return mContext;
    }

    public static synchronized MovieApplication getInstance() {
        return mInstance;

    }

    public  Movies getMovies() {
        return mMovies;
    }

    public void setMovies(Movies mMovies) {
        MovieApplication.mMovies = mMovies;
    }


    public  String getSortPreference() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);


            return prefs.getString(SORT_TYPE, "popularity");

    }

    public void setSortPreference(String sortPreference) {
        SharedPreferences.Editor editor = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(SORT_TYPE,sortPreference);
        editor.apply();

    }
    RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public static MovieDetail getMovieDetail() {
        return mMovieDetail;
    }

    public static void setMovieDetail(MovieDetail mMovieDetail) {
        MovieApplication.mMovieDetail = mMovieDetail;
    }

    public void setRequestType(String requestType) {
        SharedPreferences.Editor editor = this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(REQUEST_TYPE,requestType);
        editor.apply();
    }
    public String getRequestType() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);


            return prefs.getString(REQUEST_TYPE, mContext.getString(R.string.method_volley));

    }
}
