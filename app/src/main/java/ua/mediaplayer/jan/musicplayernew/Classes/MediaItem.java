package ua.mediaplayer.jan.musicplayernew.Classes;

import android.util.Log;
// class that has information about the song
public class MediaItem {
    private String title; //song title
    private String artist;// author
    private String album;//album title
    private String path; //the path to the location song
    private long duration;// the length of the song
    private long albumId; // id album title


    public MediaItem(String title, String artist, String album, String path, long duration, long albumId) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.path = path;
        this.duration = duration;
        this.albumId = albumId;
    }


    public MediaItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaItem item = (MediaItem) o;
        if (duration != item.duration) return false;
        if (!title.equals(item.title)) return false;
        if (!artist.equals(item.artist)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + artist.hashCode();
        result = 31 * result + album.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (int) (albumId ^ (albumId >>> 32));
        return result;
    }
}
