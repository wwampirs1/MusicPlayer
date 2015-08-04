package ua.mediaplayer.jan.musicplayernew.Classes;

import java.io.FileDescriptor;
import java.util.ArrayList;

import ua.mediaplayer.jan.musicplayernew.MusicData;
import ua.mediaplayer.jan.musicplayernew.R;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

public class UtilFunctions implements MusicData {
    /**
     * Check if service is running or not
     */
    public static boolean isServiceRunning(String serviceName, Context context) {
        Log.d("myLogs", "UtilFunctions isServiceRunning");
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                Log.d("myLogs", "UtilFunctions isServiceRunning true");
                return true;
            }
        }
        Log.d("myLogs", "UtilFunctions isServiceRunning false");
        return false;
    }

    /**
     * Read the songs present in external storage
     */
    public static ArrayList<MediaItem> listOfSongs(Context context) {
        Log.d("myLogs", "UtilFunctions ADDlistOfSongs");

//context.
        Cursor c = PlayerConstants.FOLDER_NAME == null ?
                context.getContentResolver().query(MUSICK_URI, null, IS_MUSIC + " != 0", null, null) :
                context.getContentResolver().query(MUSICK_URI, null, DATA + " like ? ", new String[]{"%/" + PlayerConstants.FOLDER_NAME + "%"}, null);

          ArrayList<MediaItem> listOfSongs = new ArrayList<MediaItem>();
        if (c.moveToFirst()) {
            PlayerConstants.SONGS_LIST.clear();
            do {
                listOfSongs.add(new MediaItem(c.getString(c.getColumnIndex(TITLE)),
                        c.getString(c.getColumnIndex(ARTIST)).replace("<unknown>","Unknown"),
                        c.getString(c.getColumnIndex(ALBUM)).replace("<unknown>", "Unknown"),
                        c.getString(c.getColumnIndex(DATA)).replace("<unknown>", "Unknown"),
                        c.getLong(c.getColumnIndex(DURATION)),
                        c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return listOfSongs;
    }

    /**
     * Get the album image from albumId
     */
    public static Bitmap getAlbumart(Context context, Long album_id) {
        Log.d("myLogs", "UtilFunctions getAlbumart");
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
           // final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id);
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
                pfd = null;
                fd = null;
            }
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

       /**
     * Convert milliseconds into time hh:mm:ss
     */
    public static String getDuration(long milliseconds) {
        //Log.d("myLogs", "UtilFunctions getDuration");
        long sec = (milliseconds / 1000) % 60;
        long min = (milliseconds / (60 * 1000)) % 60;
        long hour = milliseconds / (60 * 60 * 1000);

        String s = (sec < 10) ? "0" + sec : "" + sec;
        String m = (min < 10) ? "0" + min : "" + min;
        String h = "" + hour;

        String time = "";
        if (hour > 0) {
            time = h + ":" + m + ":" + s;
        } else {
            time = m + ":" + s;
        }
        return time;
    }

    public static boolean currentVersionSupportBigNotification() {
        Log.d("myLogs", "UtilFunctions currentVersionSupportBigNotification");
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    public static boolean currentVersionSupportLockScreenControls() {
        Log.d("myLogs", "UtilFunctions currentVersionSupportLockScreenControls");
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return true;
        }
        return false;
    }
}
