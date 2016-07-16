package com.typingeek.xadahiya.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesListFragment extends Fragment {

    public  GridAdapter gridAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Movie[] test_movies = {
                new Movie(true,"http://image.tmdb.org/t/p/w185///nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "Interstellar", "fjskadfjlasfd",Float.parseFloat("4.5"),Float.parseFloat("44.5"), 100, "http://image.tmdb.org/t/p/w185///nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),
                new Movie(true,"http://image.tmdb.org/t/p/w185///nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "Interstellar", "fjskadfjlasfd",Float.parseFloat("4.5"),Float.parseFloat("44.5"), 100, "http://image.tmdb.org/t/p/w185///nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"),

        };

        List<Movie> movieList = new ArrayList<>(

                Arrays.asList(test_movies)
        );


        View v = inflater.inflate(R.layout.activity_movies_list_fragment, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.movies_gridview);
        gridAdapter = new GridAdapter(getActivity(), movieList);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return v;

    }

    public void UpdateMovies(){
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
    }

    @Override
    public void onStart(){
        super.onStart();
        UpdateMovies();
    }

    private Movie[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULT = "results";
        final String OWM_ADULT = "adult";
        final String OWM_BACKDROP_PATH = "backdrop_path";
        final String OWM_TITLE = "original_title";
        final String OWM_DESCRIPTION = "overview";
        final String OWM_BACKDROP_IMG = "poster_path";
        final String OWM_POPULARITY = "popularity";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_VOTE_COUNT = "vote_count";

        JSONObject forecastJson = new JSONObject(movieJsonStr);
        JSONArray resultArray = forecastJson.getJSONArray(OWM_RESULT);
        Movie[] movie_list = new Movie[resultArray.length()];
        for(int i = 0; i < resultArray.length(); i++) {
            JSONObject movieObject = resultArray.getJSONObject(i);

            boolean isAdult = movieObject.getBoolean(OWM_ADULT);
            String backdrop_url = "http://image.tmdb.org/t/p/w185/"+movieObject.getString(OWM_BACKDROP_PATH);
            String title = movieObject.getString(OWM_TITLE);
            String description = movieObject.getString(OWM_DESCRIPTION);
            String backdrop_img = "http://image.tmdb.org/t/p/w185/"+movieObject.getString(OWM_BACKDROP_IMG);
            Float popularity = Float.parseFloat(movieObject.get(OWM_POPULARITY).toString());
            Float vote_average = Float.parseFloat(movieObject.get(OWM_VOTE_AVERAGE).toString());
            Integer vote_count = movieObject.getInt(OWM_VOTE_COUNT);
            Log.d("movie", backdrop_url);
            movie_list[i] = new Movie(isAdult, backdrop_url, title, description, popularity, vote_average, vote_count, backdrop_img);

        }
        return movie_list;
    }


    public class FetchMovies extends AsyncTask<String, Void, Movie[]> {
        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        protected Movie[] doInBackground(String... urls) {

            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String movieJsonstr = null;
            String format = "json";


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular?";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, "b9c5a823d73daf11cb998036021c8c20")
                        .build();

                URL url = new URL(builtUri.toString());

//                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonstr = buffer.toString();
                Log.e(LOG_TAG, movieJsonstr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getMovieDataFromJson(movieJsonstr);
            }
            catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Movie[] result) {
//            super.onPostExecute(strings);
            if (result != null){
                gridAdapter.clear();
                for (Movie movie : result){
                    gridAdapter.add(movie);
                }
            }

        }
        }
    }


