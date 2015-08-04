package ua.mediaplayer.jan.musicplayernew.Classes;

import android.content.Context;
import android.util.Log;

import ua.mediaplayer.jan.musicplayernew.Classes.PlayerConstants;
import ua.mediaplayer.jan.musicplayernew.Classes.UtilFunctions;
import ua.mediaplayer.jan.musicplayernew.R;
import ua.mediaplayer.jan.musicplayernew.SongService;

public class Controls {
// play song
    public static void playControl(Context context) {
        Log.d("myLogs", "Controls playControl");
        sendMessage(context.getResources().getString(R.string.play));
    }
// pause song
    public static void pauseControl(Context context) {
        Log.d("myLogs", "Controls pauseControl");
        sendMessage(context.getResources().getString(R.string.pause));
    }
// next song
    public static void nextControl(Context context) {
        Log.d("myLogs", "Controls nextControl");
        if (!UtilFunctions.isServiceRunning(SongService.class.getName(), context))
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                PlayerConstants.SONG_NUMBER++;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }
//previous song
    public static void previousControl(Context context) {
        Log.d("myLogs", "Controls previousControl");
        if (!UtilFunctions.isServiceRunning(SongService.class.getName(), context))
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER > 0) {
                PlayerConstants.SONG_NUMBER--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LIST.size() - 1;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
    }
// send message
    private static void sendMessage(String message) {
        Log.d("myLogs", "Controls sendMessage");
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
        }
    }
}
