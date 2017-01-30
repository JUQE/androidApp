

package com.example.brianofrim.juqe;

        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.support.v4.content.ContextCompat;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

/**
 * Created by jferris on 28/01/17.
 */

public class ResultsListAdapter extends ArrayAdapter<Song> {

    private Context context;
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<String> keyList= new ArrayList<>();


    public ResultsListAdapter(Context context, ArrayList<Song> songList) {
        super(context, 0, songList);
        this.context = context;
        this.songList = songList;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_results_listview, parent, false);
        }

//        ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
//
//        if(imageView != null) {
//            new ImageDownloaderTask(imageView).execute(songList.get(position).getAlbumArt());
//        }

        TextView songName = (TextView) convertView.findViewById(R.id.songName);
        TextView songArtist = (TextView) convertView.findViewById(R.id.songArtist);
        final Button addToPoolButton = (Button) convertView.findViewById(R.id.add_to_pool);
        setButtonColor(addToPoolButton, songList.get(position), false);
        addToPoolButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VenueController.addSong(songList.get(position));
                setButtonColor(addToPoolButton, songList.get(position), true);
                notifyDataSetChanged();
            }
            //RelativeLayout listItem = (RelativeLayout) v.getParent();
            //listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.
        });

        Song song = getItem(position);
        //Set color for completion

        songName.setText(song.getName());
        songArtist.setText(song.getArtist());
        return convertView;

    }

    public void setButtonColor(Button button, Song song, Boolean pressed) {
        if(VenueController.getURIList().contains(song.getURI())) {
            button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.buttonPressed), PorterDuff.Mode.MULTIPLY);
            button.setEnabled(false);
        } else if(pressed){
            button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.buttonPressed), PorterDuff.Mode.MULTIPLY);
            button.setEnabled(false);
            VenueController.addURI(song.getURI());
        } else {
            button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.tertiary_colour), PorterDuff.Mode.MULTIPLY);
            button.setEnabled(true);
        }
    }
}
