package thundersharp.aigs.spectre.core.utils;

import com.google.android.gms.maps.model.LatLng;

public class LatLongConverter {

    public static LatLongConverter initialize(){
        return new LatLongConverter();
    }

    public LatLng getlatlang(String lat_long) {
        double lat = Double.parseDouble(lat_long.substring(0,lat_long.indexOf(",") ));
        double longitude = Double.parseDouble(lat_long.substring(lat_long.indexOf(",")+1));
        return new LatLng(lat,longitude);
    }

    public double getlat(String lat_long) {
        return Double.parseDouble(lat_long.substring(0,lat_long.indexOf(",") ));
    }

    public double getlang(String lat_long) {
        return Double.parseDouble(lat_long.substring(lat_long.indexOf(",")+1));
    }
}
