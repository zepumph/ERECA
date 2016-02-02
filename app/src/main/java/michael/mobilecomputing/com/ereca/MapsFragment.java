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

    private final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private final LatLng KIEL = new LatLng(53.551, 9.993);

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
        Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                .title(locationNameString));
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(KIEL)
                .title(locationNameString)
                .snippet(locationNameString + locationNameString)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher)));
        kiel.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

}

