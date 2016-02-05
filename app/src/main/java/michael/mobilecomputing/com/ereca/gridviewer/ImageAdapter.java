package michael.mobilecomputing.com.ereca.gridviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import michael.mobilecomputing.com.ereca.R;

/**
 * Created by Xander on 1/28/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    Integer[] mThumbIds;
    public ImageAdapter(Context c, Integer[] imageArray) {
        mContext = c;
        mThumbIds = imageArray;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new LinearLayout for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        /* inflate a note view */
        LinearLayout note_view;
        /* set up the image */
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            /* inflate a note view */
            note_view = (LinearLayout)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.note_view, null, false);
            imageView = (ImageView) note_view.getChildAt(0);
            //imageView.setScaleType(ImageView.ScaleType.);

            /* set height + width of view */
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));

            /* set true if you want imageview to adjust its bounds to preserve aspect ratio of its drawable */
            imageView.setAdjustViewBounds(false);

            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            //imageView = (ImageView) convertView;
            imageView = (ImageView) ((LinearLayout)convertView).getChildAt(0);
            note_view = (LinearLayout) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);

        return note_view;

    }



}
