package com.app.kiory.spotify;

/**
 * Created by darknoe on 12/6/15.
 */

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.kiory.spotify.database.DataContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DETAIL_LOADER = 0;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private String artistName = "";
    private MediaPlayer mediaPlayer;
    private ImageButton playButton;

    public TrackDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null){
            mUri = arguments.getParcelable(TrackDetailFragment.DETAIL_URI);

            String artistSpotifyId = DataContract.TrackEntry.getTrackSpotifyId(mUri);
            Cursor artistNameCursor = getActivity().getContentResolver().query(DataContract.ArtistEntry.buildArtistNameUri(artistSpotifyId), null, null, null, null);

            if ((artistNameCursor != null) && artistNameCursor.moveToFirst()) {
                artistName= artistNameCursor.getString(artistNameCursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_NAME));
            }
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                mUri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        String track = data.getString(data.getColumnIndex(DataContract.TrackEntry.COLUMN_NAME));
        String album = data.getString(data.getColumnIndex(DataContract.TrackEntry.COLUMN_ALBUM));
        Uri previewUrl = Uri.parse(data.getString(data.getColumnIndex(DataContract.TrackEntry.COLUMN_PREVIEW_URL)));

        TextView albumTextView = (TextView) getView().findViewById(R.id.detail_album);
        TextView trackTextView = (TextView) getView().findViewById(R.id.detail_track);
        TextView artistTextView = (TextView) getView().findViewById(R.id.detail_artist);
        ImageView albumThumbnail = (ImageView) getView().findViewById(R.id.detail_album_thumbnail);
        playButton = (ImageButton) getView().findViewById(R.id.detail_button_play);

        trackTextView.setText(track);
        artistTextView.setText(artistName);
        albumTextView.setText(album);

        String img_url = data.getString(data.getColumnIndex(DataContract.TrackEntry.COLUMN_THUMBNAIL));
        if(img_url.compareTo("")!=0)
            Picasso.with(getActivity()).load(img_url).into(albumThumbnail);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), previewUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton.setTag(new Boolean(false));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ((Boolean)playButton.getTag())==false ) {
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    playButton.setTag(new Boolean(true));
                    playButton.setImageResource(R.drawable.ic_av_pause);
                }else{
                    playButton.setTag(new Boolean(false));
                    playButton.setImageResource(R.drawable.ic_av_play_arrow);
                    mediaPlayer.stop();
                }
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
