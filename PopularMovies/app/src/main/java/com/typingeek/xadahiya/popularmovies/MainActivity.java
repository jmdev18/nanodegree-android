package com.typingeek.xadahiya.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private String MOVIEFRAGMENT_TAG = "MF_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_details_container) != null){
            mTwoPane = true;

            if( savedInstanceState == null){
                DetailFragment detailFragment = new DetailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_details_container, detailFragment, MOVIEFRAGMENT_TAG).commit();
            }
        }
        else{
            mTwoPane = false;
        }

        MoviesListFragment moviesListFragment = (MoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.grid_fragment);
        moviesListFragment.setTwoPaneLayout(mTwoPane);
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

//        if(fragment == null){
//            fragment = new MoviesListFragment();
//            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
