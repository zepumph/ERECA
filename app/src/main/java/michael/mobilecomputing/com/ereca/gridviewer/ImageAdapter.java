package michael.mobilecomputing.com.ereca.gridviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import michael.mobilecomputing.com.ereca.Note;
import michael.mobilecomputing.com.ereca.R;

/**
 * Created by Xander on 1/28/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //Integer[] mThumbIds;
    ArrayList<LinearLayout> notes;
    ArrayList<Note> note_objects;


    //public ImageAdapter(Context c, Integer[] imageArray) {
    public ImageAdapter(Context c){
        mContext = c;
        //mThumbIds = imageArray;
        notes = new ArrayList<>();
        note_objects = new ArrayList<>();
        //registerDataSetObserver(new MyDataSetObserver());
    }

    public int getCount() {

        //return mThumbIds.length;
        return notes.size();
    }

    public void addNote(LinearLayout note){
        notes.add(note);
    }


    public void addNote(Note note){
        /* inflate a note view */
        LinearLayout note_view;
        /* set up the image */
        ImageView imageView;
        TextView textView;

        /* inflate a note view */
        note_view = (LinearLayout)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.note_view, null, false);
        imageView = (ImageView) note_view.getChildAt(0);



        /* set true if you want imageview to adjust its bounds to preserve aspect ratio of its drawable */
        imageView.setAdjustViewBounds(true);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);

        imageView.setImageBitmap(note.getImage());

        /* set the note text */
        textView = (TextView) note_view.getChildAt(1);
        textView.setText(note.getNoteText());

        /* add to note object so the detail activity can access them */
        note_objects.add(note);

        /* adds the note to the gridview should be called at the end
        *   be aware that the note will note be displayed until gridView.invalidateViews() is called */
        addNote(note_view);
    }

    public Object getItem(int position) {

        return note_objects.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new LinearLayout for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return notes.get(position);

    }



}
