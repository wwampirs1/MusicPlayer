package ua.mediaplayer.jan.musicplayernew.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.mediaplayer.jan.musicplayernew.Classes.MediaItem;
import ua.mediaplayer.jan.musicplayernew.Classes.PlayerConstants;
import ua.mediaplayer.jan.musicplayernew.Classes.UtilFunctions;
import ua.mediaplayer.jan.musicplayernew.R;

public class CustomAdapter extends ArrayAdapter<MediaItem> {

    ArrayList<MediaItem> listOfSongs;
    ArrayList<MediaItem> originalList;
    Context context;
    LayoutInflater inflator;
    ViewHolder holder;
    private SongFilter friendFilter;

    public CustomAdapter(Context context, int resource, ArrayList<MediaItem> listOfSongs) {
        super(context, resource, listOfSongs);
        //Log.d("myLogs", "CustomAdapter Constructor");
        this.listOfSongs = listOfSongs;
        this.context = context;
        inflator = LayoutInflater.from(context);
        originalList = new ArrayList<MediaItem>();
        originalList.addAll(listOfSongs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Log.d("myLogs", "CustomAdapter getView");
        View view=convertView;
        if (view == null) {
            view = inflator.inflate(R.layout.custom_list, parent, false);
            holder = new ViewHolder();
            holder.tvSongName = (TextView) view.findViewById(R.id.tvSongName);
            holder.tvArtist = (TextView) view.findViewById(R.id.tvArtist);
            holder.tvAlbum = (TextView) view.findViewById(R.id.tvAlbum);
            holder.tvDuration = (TextView) view.findViewById(R.id.tvDuration);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MediaItem song = listOfSongs.get(position);
        holder.tvSongName.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvAlbum.setText(song.getAlbum());
        holder.tvDuration.setText(UtilFunctions.getDuration(song.getDuration()));
        return view;
    }

    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new SongFilter();
        }
        return friendFilter;
    }

    private class ViewHolder {
        TextView tvSongName;
        TextView tvArtist;
        TextView tvAlbum;
        TextView tvDuration;
    }

    private class SongFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            try {
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<MediaItem> tempList = new ArrayList<MediaItem>();
                    // search content in  list
                    for (MediaItem mediaItem : originalList) {
                        if (mediaItem.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                mediaItem.getAlbum().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                mediaItem.getArtist().toLowerCase().contains(constraint.toString().toLowerCase()))
                            tempList.add(mediaItem);

                    }
                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                } else {
                    filterResults.count = originalList.size();
                    filterResults.values = originalList;
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("myLogs", "CustomAdapter  SongFilter performFiltering " + e.toString());
            }
            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         *
         * @param constraint text
         * @param results    filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                listOfSongs = (ArrayList<MediaItem>) results.values;
                clear();
                for (MediaItem mediaItem : listOfSongs)
                    add(mediaItem);
                notifyDataSetInvalidated();
            } catch (Exception e) {
                Log.d("myLogs", "CustomAdapter  SongFilter performFiltering " + e.toString());
            }
        }
    }
}
