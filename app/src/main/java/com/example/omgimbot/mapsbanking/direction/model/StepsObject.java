package com.example.omgimbot.mapsbanking.direction.model;

public class StepsObject {
    private PolylineObject polyline;
    private PolylineObject duration;
    public StepsObject(PolylineObject polyline) {
        this.polyline = polyline;
    }
    public PolylineObject getPolyline() {
        return polyline;
    }

    public PolylineObject getDuration() {
        return duration;
    }
}
