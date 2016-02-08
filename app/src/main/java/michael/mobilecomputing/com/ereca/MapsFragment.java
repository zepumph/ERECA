package michael.mobilecomputing.com.ereca;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.regex.Pattern;

public class MapsFragment extends SupportMapFragment implements
        OnMapReadyCallback {

    private String lat;
    private String lon;
    private Note noteToDisplay;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap map;
    private Marker marker;
    public MapsFragment() {
        //locationNameString = name;
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (map == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getArguments();
        String latLonString = bundle.getString("noteLatLon");
        String[] latLon = latLonString.split(Pattern.quote("|"));
        lat = latLon[0];
        lon = latLon[1];
        map = googleMap;
        if (lat != null && lon != null)
            setUpMap();
    }

    private void setUpMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        LatLng latLng = new LatLng(new Double(lat),new Double(lon));
        Marker cc = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("")
                .snippet(""));
        cc.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

    }

}

