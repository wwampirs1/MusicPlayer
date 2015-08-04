package ua.mediaplayer.jan.musicplayernew.Classes;

public class Folder implements Comparable<Folder> {
    private String name;
    private String data;
    private String path;//
    private int image; // image

    public Folder(String name, String data, String path, int image) {
        this.name = name;
        this.data = data;
        this.path = path;
        this.image = image;
    }

    public Folder(String name, String path, int image) {
        this.name = name;
        this.path = path;
        this.image = image;
    }

    public Folder(String name, String data, String path) {
        this.name = name;
        this.data = data;
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(Folder o) {
        if (this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
