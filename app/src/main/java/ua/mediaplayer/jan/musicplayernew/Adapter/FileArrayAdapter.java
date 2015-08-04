package ua.mediaplayer.jan.musicplayernew.Adapter;

/**
 * Created by Julia on 08.07.2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ua.mediaplayer.jan.musicplayernew.Classes.Folder;
import ua.mediaplayer.jan.musicplayernew.R;


public class FileArrayAdapter extends ArrayAdapter<Folder> {

    private Context context;
    private int layout;
    private List<Folder> items;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Folder> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        layout = textViewResourceId;
        items = objects;
    }

    public Folder getItem(int i) {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.tvNameFolder = (TextView) convertView.findViewById(R.id.tvNameFolder);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Folder f = items.get(position);
        if (f != null) {
            holder.tvNameFolder.setText(f.getName());
            holder.ivIcon.setImageResource(f.getImage());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvNameFolder;
        ImageView ivIcon;

    }

}

