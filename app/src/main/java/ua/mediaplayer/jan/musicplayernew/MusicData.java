package ua.mediaplayer.jan.musicplayernew;

import android.net.Uri;
import android.provider.MediaStore;
//interface with data on Songs of information
public interface MusicData {
    String TITLE = android.provider.MediaStore.Audio.Media.TITLE;
    String ARTIST = android.provider.MediaStore.Audio.Media.ARTIST;
    String ALBUM = android.provider.MediaStore.Audio.Media.ALBUM;
    String DURATION = android.provider.MediaStore.Audio.Media.DURATION;
    String DATA = android.provider.MediaStore.Audio.Media.DATA;
    Uri MUSICK_URI = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    String IS_MUSIC= MediaStore.Audio.Media.IS_MUSIC;


}

