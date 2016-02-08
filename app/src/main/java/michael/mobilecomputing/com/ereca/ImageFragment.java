package michael.mobilecomputing.com.ereca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private TextView noteTextView;
    private TextView locationTextView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String noteString;
    private static Bitmap bitmap;
    private static FrameLayout textViewLayout;
    private static FrameLayout parentFrameLayout;
    //    public ImageFragment(String noteString) {
//        this.noteString = noteString;
//    }
    public ImageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageFragment newInstance(int sectionNumber, String note) {
        //if (sectionNumber == 0) {
        noteString = note;
        bitmap = BitmapFactory.decodeFile("/sdcard/" + "detailImage" + ".png");

        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        noteTextView = (TextView) rootView.findViewById(R.id.note);
        if (noteString != null)
            noteTextView.setText(noteString);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        textViewLayout = (FrameLayout) rootView.findViewById(R.id.textViewFrame);
        textViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewLayout.getVisibility() == View.VISIBLE)
                    textViewLayout.setVisibility(View.INVISIBLE);
            }
        });
        parentFrameLayout = (FrameLayout) rootView.findViewById(R.id.parentFrameLayout);
        parentFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewLayout.getVisibility() == View.INVISIBLE)
                    textViewLayout.setVisibility(View.VISIBLE);
            }
        });
        //imageView.setImageBitmap(bitmap);
        return rootView;
    }
}