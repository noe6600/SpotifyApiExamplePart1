package com.app.kiory.spotify;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.app.kiory.spotify.database.DataContract;


public class DetailActivity extends ActionBarActivity implements DetailFragment.Callbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(getResources().getString(R.string.top_tracks_title));

        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

        String artistSpotifyId = DataContract.TrackEntry.getTrackSpotifyId(getIntent().getData());
        String artistName = "";

        Cursor artistNameCursor = getContentResolver().query(DataContract.ArtistEntry.buildArtistNameUri(artistSpotifyId), null, null, null, null);

        if ((artistNameCursor != null) && artistNameCursor.moveToFirst()) {
            artistName= artistNameCursor.getString(artistNameCursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_NAME));
        }
        getSupportActionBar().setSubtitle(artistName);

        if (savedInstanceState == null) {



            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        Intent intent = new Intent(this, TrackDetailActivity.class).setData(contentUri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
