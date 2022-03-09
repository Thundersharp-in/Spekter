package thundersharp.aigs.spectre.core.helpers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import thundersharp.aigs.spectre.core.models.MarkersData;

public class MapsHelpers {

    private static MapsHelpers mapsHelpers = null;
    private GoogleMap googleMap;
    private MarkersData[] mapsLocations;

    public static MapsHelpers getInstance(){
        if (mapsHelpers == null) mapsHelpers = new MapsHelpers();
        return mapsHelpers;
    }

    public MapsHelpers(){}

    public MapsHelpers setGoogleMaps(GoogleMap googleMap){
        this.googleMap = googleMap;
        return this;
    }

    public MapsHelpers setMarkers(MarkersData... coOrdinates){
        this.mapsLocations = coOrdinates;
        return this;
    }

    public synchronized Marker[] locate(){
        Marker[] marker = new Marker[mapsLocations.length];
        for (int i = 0; i < mapsLocations.length; i++) {
            marker[i] = googleMap.addMarker(new MarkerOptions()
                    .title(mapsLocations[i].message)
                    .position(new LatLng(mapsLocations[i].lat,mapsLocations[i].longitude)));
        }

        return marker;
    }

}
