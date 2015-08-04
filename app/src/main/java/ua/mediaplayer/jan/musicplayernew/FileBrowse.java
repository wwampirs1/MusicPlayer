package ua.mediaplayer.jan.musicplayernew;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.mediaplayer.jan.musicplayernew.Adapter.FileArrayAdapter;
import ua.mediaplayer.jan.musicplayernew.Classes.Folder;
import ua.mediaplayer.jan.musicplayernew.Classes.PlayerConstants;
import ua.mediaplayer.jan.musicplayernew.MusicData;
import ua.mediaplayer.jan.musicplayernew.R;

public class FileBrowse extends ListActivity implements View.OnClickListener, MusicData {

    private File currentDir; // file now
    private FileArrayAdapter adapter; // adapter files
    private Button btnFolderData;
    private Folder folder=new Folder();//folder music
    private String path; // selected folder

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser);
        load();
        initializeComponents();
    }

    // initializing components
    private void initializeComponents() {
        btnFolderData = (Button) findViewById(R.id.btnSelect);
        btnFolderData.setOnClickListener(this);
        currentDir = new File(path);
        fill(currentDir);
    }

    @Override
    protected void onDestroy() {
        save();//save data
        super.onDestroy();
    }

    private void fill(File f) {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Folder> dir = new ArrayList<Folder>();
        List<Folder> fls = new ArrayList<Folder>();
        try {
            for (File file : dirs) {
                if (file.isDirectory()) {
                    dir.add(new Folder(file.getName(),
                            file.getAbsolutePath(),
                            R.drawable.folder
                    ));
                } else {
                    if (getFileType(file.getName()).equals("mp3") ||
                            getFileType(file.getName()).equals("3gp") ||
                            getFileType(file.getName()).equals("m4a") ||
                            getFileType(file.getName()).equals("wma") ||
                            getFileType(file.getName()).equals("flac") ||
                            getFileType(file.getName()).equals("ogg") ||
                            getFileType(file.getName()).equals("m4p"))
                        fls.add(new Folder(file.getName(),
                                file.getAbsolutePath(),
                                R.drawable.note));
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("/"))
            dir.add(0, new Folder("..", "Parent Directory", f.getParent()));
        adapter = new FileArrayAdapter(this, R.layout.file, dir);
        this.setListAdapter(adapter);
    }

    ///click on the component list
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            Folder folder = adapter.getItem(position);
            if (folder.getImage() == R.drawable.folder || folder.getName().equalsIgnoreCase("..")) {
                currentDir = new File(folder.getPath());
                path = folder.getPath();
                fill(currentDir);
                this.folder = folder;
            }
        } catch (Exception e) {
        }
    }

    //file extension
    private String getFileType(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public void onClick(View view) {
        if (folder != null) {
            Cursor cursor = getContentResolver().query(MUSICK_URI, null, DATA + " like ? ",
                    new String[]{"%/" + folder.getName() + "%"}, null);
            if (cursor.getCount() != 0) {
                PlayerConstants.FOLDER_NAME = folder.getName();
                cursor.close();
                setResult(RESULT_OK);
                finish();

            } else
                Toast.makeText(getApplicationContext(), "Wrong folder. In the folder does not have songs", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Wrong folder. In the folder does not have songs", Toast.LENGTH_SHORT).show();
    }

    //save data
    private void save() {
        Log.d("myLogs", "FileBrowse save");
        SharedPreferences sPref = getSharedPreferences("fileData", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("path", path);
        ed.putString("folderName", folder.getName());
        ed.putString("folderPath", folder.getPath());
        ed.commit();
    }

    //save load
    private void load() {
        Log.d("myLogs", "FileBrowse load");
        SharedPreferences sPref = getSharedPreferences("fileData", MODE_PRIVATE);
        path = sPref.getString("path", "/");
        folder.setPath(sPref.getString("folderPath", null));
        folder.setName(sPref.getString("folderName", null));
    }

}