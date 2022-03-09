package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class MarkersData implements Serializable {
    public double lat;
    public  double longitude;
    public String message;

    public MarkersData(){}

    public MarkersData(double lat, double longitude, String message) {
        this.lat = lat;
        this.longitude = longitude;
        this.message = message;
    }
}
