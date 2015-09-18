package nanodegree.rahall.popularmovies1.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import nanodegree.rahall.popularmovies1.models.Movie;
import nanodegree.rahall.popularmovies1.models.MovieDetail;
import nanodegree.rahall.popularmovies1.models.Movies;

/**
 * Created by rahall on 8/23/15.
 */
public class Conversion {

    public static Movies convertJsonArrayToMovieModelArray(JSONArray jsonArrayMovies) throws org.json.JSONException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Movies movies = new Movies();
        for (int i = 0; i < jsonArrayMovies.length(); i++) {
            JSONObject jsonMovie = jsonArrayMovies.getJSONObject(i);
            Movie movie = gson.fromJson(String.valueOf(jsonMovie),Movie.class);
            movies.addMovie(movie);
        }
        return movies;

    }

    public static MovieDetail  convertJsonObjectToMovieDetailModel(JSONObject jsonMovieDetail) throws org.json.JSONException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();



        MovieDetail movieDetail = gson.fromJson(String.valueOf(jsonMovieDetail),MovieDetail.class);
        return movieDetail;

    }
}
