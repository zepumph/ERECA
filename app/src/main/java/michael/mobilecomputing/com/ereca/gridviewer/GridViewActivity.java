package michael.mobilecomputing.com.ereca.gridviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import michael.mobilecomputing.com.ereca.R;
import pl.polidea.view.ZoomView;



public class GridViewActivity extends Activity {

    GridView gridview;
    ZoomView zoomView;
    LinearLayout main_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);


        /* from xander's code */
        /* Zoom View */
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoomable_view, null, false);
        //v.setLayoutParams(new LinearLayout.LayoutParams(GridView.LayoutParams.FILL_PARENT, GridView.FILL_PARENT));

        zoomView = new ZoomView(this);
        zoomView.addView(v);

        main_container = (LinearLayout) findViewById(R.id.main_container);
        main_container.addView(zoomView);

//        /* nav bar */
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(GridViewActivity.this);


        /* Grid View */
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GridViewActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                //gridview.setColumnWidth(gridview.getColumnWidth()+5);
            }
        });
    }



    @Override
    public void onBackPressed() {
        GridViewActivity.this.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
       return false;
    }




}
