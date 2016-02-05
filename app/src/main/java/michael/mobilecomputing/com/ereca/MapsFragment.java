package michael.mobilecomputing.com.ereca;

import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

public class MapsFragment extends SupportMapFragment implements
        OnMapReadyCallback {

    private final LatLng CC = new LatLng(38.847045, -104.824827);

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap map;
    private Marker marker;
    private String locationNameString = "MORTAL KOMBAT";
    public MapsFragment() {
        //locationNameString = name;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("MyMap", "onResume");
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (map == null) {

            Log.d("MyMap", "setUpMapIfNeeded");

            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MyMap", "onMapReady");
        map = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        Marker cc = map.addMarker(new MarkerOptions()
                .position(CC)
                .title(locationNameString)
                .snippet(locationNameString + locationNameString));
        cc.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(CC, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

    }

}

