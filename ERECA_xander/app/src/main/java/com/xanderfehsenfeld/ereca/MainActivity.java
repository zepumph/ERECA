package com.xanderfehsenfeld.ereca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/* created by Xander Fehsenfeld
*       An activity which allows the user to take a picture and store the picture in a horizontal scroller
 */

public class MainActivity extends Activity {

    /* Request code for taking a picture */
    static final int TAKE_PICTURE_ID = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /** doToast
     *      do a short toast notification with the input string
     * @param input the text of the toast notification
     */
    private void doToast( String input ){
        Toast toast =
                Toast.makeText( getApplicationContext(), input, Toast.LENGTH_SHORT );
        toast.show();
    }

    /* called when button is pushed */
    public void pushThumbNail( View view) {
        doToast( "thumbnail clicked!");
        ImageView imager = (ImageView)findViewById(R.id.main_imager);
        imager = cloneImageView( (ImageView)view );
    }

    /** cloneImageView
     *      returns a new ImageView with layoutparams of input
     * @param iv
     * @return
     */
    private ImageView cloneImageView( ImageView iv ){
        ImageView output = new ImageView(MainActivity.this);
        output.setLayoutParams(iv.getLayoutParams());
        return output;
    }
    /** callback
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult( int requestCode, int resultCode, Intent data){

        /* get the resulting bitmap and store on screen */
        if ( requestCode == TAKE_PICTURE_ID ) {
            /* ref the linear layout which is the child of the horizontal scroller */
            LinearLayout scrollLayout = (LinearLayout) findViewById( R.id.scroll );

            //ImageView toAdd;
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");

            /* the view to be added to the horizontal scroller */
            ImageView toAdd;
            toAdd = (ImageView)scrollLayout.getChildAt(0);

            /* if first image */
            if (toAdd.getVisibility() == ImageView.INVISIBLE){
                toAdd.setVisibility(ImageView.VISIBLE);

            } else {
                toAdd = cloneImageView( (ImageView) toAdd );
                scrollLayout.addView(toAdd);

            }


            //toAdd = cloneImageView( (ImageView) findViewById(R.id.default_view) );

            toAdd.setImageBitmap(bm);

            /* set imager to last image taken */
            ImageView imager = (ImageView)findViewById(R.id.main_imager);
            imager.setImageBitmap(bm);

        }
    }

    /** take a photograph
     *
     * @param view
     */
    public void photograph(View view){
        Intent startCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(startCam, TAKE_PICTURE_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}
