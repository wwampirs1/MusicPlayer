package ua.mediaplayer.jan.musicplayernew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import ua.mediaplayer.jan.musicplayernew.Classes.Controls;
import ua.mediaplayer.jan.musicplayernew.Classes.PlayerConstants;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myLogs", "NotificationBroadcast onReceive");
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (!PlayerConstants.SONG_PAUSED) {
                        Controls.pauseControl(context);
                    } else {
                        Controls.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("myLogs", "NotificationBroadcast onReceive: KEYCODE_MEDIA_NEXT");
                    Controls.nextControl(context);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("myLogs", "NotificationBroadcast onReceive: KEYCODE_MEDIA_PREVIOUS");
                    Controls.previousControl(context);
                    break;
            }
        } else {
            if (intent.getAction().equals(SongService.NOTIFY_PLAY)) {
                Controls.playControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_PAUSE)) {
                Controls.pauseControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_NEXT)) {
                Controls.nextControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_DELETE)) {
                context.stopService(new Intent(context, SongService.class));
            } else if (intent.getAction().equals(SongService.NOTIFY_PREVIOUS)) {
                Controls.previousControl(context);
            }
        }
    }

    public String ComponentName() {
        Log.d("myLogs", "NotificationBroadcast ComponentName");
        return this.getClass().getName();
    }
}
