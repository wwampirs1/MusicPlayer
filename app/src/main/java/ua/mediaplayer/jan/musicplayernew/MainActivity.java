package ua.mediaplayer.jan.musicplayernew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;


import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;

import ua.mediaplayer.jan.musicplayernew.Classes.Controls;
import ua.mediaplayer.jan.musicplayernew.Classes.MediaItem;
import ua.mediaplayer.jan.musicplayernew.Classes.PlayerConstants;
import ua.mediaplayer.jan.musicplayernew.Classes.UtilFunctions;
import ua.mediaplayer.jan.musicplayernew.Adapter.CustomAdapter;

public class MainActivity extends ActionBarActivity implements MusicData {
    static LinearLayout linearLayoutPlayingSong;
    static CustomAdapter customAdapter = null;
    private static TextView playingSong;
    private static ImageButton btnPause, btnNext, btnPrevious, btnPlay;
    private static SeekBar seekBar;
    private final int REQUEST_CODE_FILE_BROWSE = 100;
    private ListView mediaListView;
    private ImageButton btnStop;
    private LinearLayout mediaLayout;
    private TextView textBufferDuration, textDuration;
    private boolean sortFlag = true;
    private boolean oneFlag = true;


    @SuppressWarnings("deprecation")
    public static void updateUI() {
        Log.d("myLogs", "MainActivity updateUI");
        try {
            seekBar.setMax((int) PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getDuration());
            MediaItem data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            playingSong.setText(data.getTitle() + " " + data.getArtist() + "-" + data.getAlbum());
            linearLayoutPlayingSong.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }
    }

    public static void changeButton() {
        Log.d("myLogs", "MainActivity changeButton");
        if (PlayerConstants.SONG_PAUSED) {
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
        } else {
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        }
    }

    public static void changeUI() {
        Log.d("myLogs", "MainActivity changeUI");
        updateUI();
        changeButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Log.d("myLogs", "MainActivity onCreate");
            load();
            init();
        } catch (Exception e) {
            Log.d("myLogs", "MainActivity " + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("myLogs", "MainActivity onResume");

        //  oneSong();
        try {
            if (UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext())) {
                updateUI();
            } else {
                stopSong();
                linearLayoutPlayingSong.setVisibility(View.GONE);
            }
            changeButton();
            PlayerConstants.SEEKBAR_HANDLER = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Integer i[] = (Integer[]) msg.obj;
                    textBufferDuration.setText(UtilFunctions.getDuration(i[0]));
                    textDuration.setText(UtilFunctions.getDuration(i[1]));
                    seekBar.setProgress(i[0]);
                }
            };
        } catch (Exception e) {
        }
    }

    private void init() {
        Log.d("myLogs", "MainActivity init");
        getViews();
        setListeners();
        playingSong.setSelected(true);
        if (PlayerConstants.SONGS_LIST.size() <= 0) {
            PlayerConstants.SONGS_LIST = UtilFunctions.listOfSongs(getApplicationContext());
        }
        setListItems();

    }

    // apdater inseert
    private void setListItems() {
        Log.d("myLogs", "MainActivity setListItems");
        customAdapter = new CustomAdapter(this, R.layout.custom_list, PlayerConstants.SONGS_LIST);
        mediaListView.setAdapter(customAdapter);
        mediaListView.setFastScrollEnabled(true);
    }

    // component add
    private void getViews() {
        Log.d("myLogs", "MainActivity getViews");
        playingSong = (TextView) findViewById(R.id.textNowPlaying);
        mediaListView = (ListView) findViewById(R.id.listViewMusic);
        mediaLayout = (LinearLayout) findViewById(R.id.linearLayoutMusicList);
        btnPause = (ImageButton) findViewById(R.id.btnPause);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        linearLayoutPlayingSong = (LinearLayout) findViewById(R.id.linearLayoutPlayingSong);
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        textBufferDuration = (TextView) findViewById(R.id.textBufferDuration);
        textDuration = (TextView) findViewById(R.id.textDuration);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    // clicking on the processing component
    private void setListeners() {
        Log.d("myLogs", "MainActivity setListeners");
        mediaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Log.d("myLogs", "MainActivity Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = position;
                if (!UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext())) {
                    Intent intentService = new Intent(getApplicationContext(), SongService.class);
                    startService(intentService);
                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }
                updateUI();
                changeButton();
                Log.d("myLogs", "MainActivity  Tapped INOUT(OUT)");

            }

        });
        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLogs", "MainActivity btnPlay");
                Controls.playControl(getApplicationContext());
            }
        });
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLogs", "MainActivity btnPause");
                Controls.pauseControl(getApplicationContext());
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLogs", "MainActivity btnNext");
                Controls.nextControl(getApplicationContext());
            }
        });
        btnPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLogs", "MainActivity btnPrevious");
                Controls.previousControl(getApplicationContext());
            }
        });
        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLogs", "MainActivity btnStop");
                stopSong();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textBufferDuration.setText(UtilFunctions.getDuration(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("myLogs", "MainActivity onStopTrackingTouch-----");
                Message msg = new Message();
                msg.arg1 = seekBar.getProgress();
                PlayerConstants.SEEKBAR_HANDLER_PULL.sendMessage(msg);
            }
        });

    }

    // stop applications
    private void stopSong() {
        Log.d("myLogs", "MainActivity stopSong");
        stopService(new Intent(getApplicationContext(), SongService.class));
        linearLayoutPlayingSong.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_song);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("myLogs", "MainActivity onQueryTextSubmit " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    customAdapter.getFilter().filter("");
                    Log.i("myLogs", "onQueryTextChange Empty String");
                    mediaListView.clearTextFilter();
                } else {
                    Log.d("myLogs", "onQueryTextChange " + newText.toString());
                    customAdapter.getFilter().filter(newText.toString());
                }
                return true;
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort) sortFlag = !sortFlag;
        switch (item.getItemId()) {
            case R.id.mTitle:
                sortListSong(R.string.name_song);
                break;
            case R.id.mAlbum:
                sortListSong(R.string.album);
                break;
            case R.id.mArtist:
                sortListSong(R.string.artist);
                break;
            case R.id.mDuration:
                sortListSong(R.string.duration);
                break;
            case R.id.addAllSongs:
                PlayerConstants.FOLDER_NAME = null;
                PlayerConstants.SONGS_LIST = UtilFunctions.listOfSongs(getApplicationContext());
                stopSong();
                break;
            case R.id.addFolderSongs:
                Intent intent = new Intent(MainActivity.this, FileBrowse.class);
                startActivityForResult(intent, REQUEST_CODE_FILE_BROWSE);
                break;
        }
        setListItems();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("myLogs", "MainActivity onActivityResult");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE_BROWSE:
                    PlayerConstants.SONGS_LIST = UtilFunctions.listOfSongs(getApplicationContext());
                    setListItems();
                    stopSong();
                    break;
            }
        } else {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }

    }

    private void sortListSong(int id) {
        switch (id) {
            case R.string.name_song:
                sortListTitle();
                break;
            case R.string.album:
                sortListAlbum();
                break;
            case R.string.artist:
                sortListArtist();
                break;
            case R.string.duration:
                sortListDuration();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("myLogs", "MainActivity onDestroy");
        save();
        super.onDestroy();
    }

    //Sort by MediaItem Title
    private void sortListTitle() {
        // stopSong();
        Collections.sort(PlayerConstants.SONGS_LIST, new Comparator<MediaItem>() {
            public int compare(MediaItem a, MediaItem b) {
                if (sortFlag) return a.getTitle().compareTo(b.getTitle());
                else return b.getTitle().compareTo(a.getTitle());
            }
        });
    }

    //Sort by: Artist
    private void sortListArtist() {
        Collections.sort(PlayerConstants.SONGS_LIST, new Comparator<MediaItem>() {
            public int compare(MediaItem a, MediaItem b) {
                if (sortFlag) return a.getArtist().compareTo(b.getArtist());
                else return b.getArtist().compareTo(a.getArtist());
            }
        });
    }

    //Sort by Album
    private void sortListAlbum() {
        Collections.sort(PlayerConstants.SONGS_LIST, new Comparator<MediaItem>() {
            public int compare(MediaItem a, MediaItem b) {
                if (sortFlag) return a.getAlbum().compareTo(b.getAlbum());
                else return b.getAlbum().compareTo(a.getAlbum());
            }
        });
    }

    //Sort by duration
    private void sortListDuration() {
        Collections.sort(PlayerConstants.SONGS_LIST, new Comparator<MediaItem>() {
            public int compare(MediaItem a, MediaItem b) {
                if (sortFlag) return (int) (a.getDuration() - b.getDuration());
                else return (int) (b.getDuration() - a.getDuration());
            }
        });
    }

    //save data
    private void save() {
        Log.d("myLogs", "MainActivity save");
        SharedPreferences sPref = getSharedPreferences("fileData", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("folderName", PlayerConstants.FOLDER_NAME);
        ed.commit();
    }

    //save load
    private void load() {
        Log.d("myLogs", "MainActivity load");
        SharedPreferences sPref = getSharedPreferences("fileData", MODE_PRIVATE);
        PlayerConstants.FOLDER_NAME = sPref.getString("folderName", null);
    }
}