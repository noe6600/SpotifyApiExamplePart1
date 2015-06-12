package com.app.kiory.spotify.database;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by darknoe on 10/6/15.
 */
public class Routes {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int _ARTISTS = 100;
    public static final int _ARTIST_NAME = 103;
    public static final int _TRACKS = 101;
    public static final int _TRACKS_ALL = 102;

    static{
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_ARTISTS, _ARTISTS);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_ARTISTS+"/*", _ARTIST_NAME);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_TRACKS+"/*", _TRACKS);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY, DataContract.PATH_TRACKS, _TRACKS_ALL);
    };

    public static int resolveRoute(Uri u){
        return sUriMatcher.match(u);
    }
}
