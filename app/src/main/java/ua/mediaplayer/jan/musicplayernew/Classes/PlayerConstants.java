package ua.mediaplayer.jan.musicplayernew.Classes;

import java.util.ArrayList;

import android.os.Handler;

public class PlayerConstants {
    //List of Songs
    public static ArrayList<MediaItem> SONGS_LIST = new ArrayList<MediaItem>();
    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true; //true
    //song changed (next, previous)
    public static boolean SONG_CHANGED = false;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song seek bar defined in Activities(MainActivity)
    public static Handler SEEKBAR_HANDLER;
    //handler for showing
    public static Handler SEEKBAR_HANDLER_PULL;
    //folder name
    public static String FOLDER_NAME;
}
