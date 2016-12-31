package net.benrowland.heatmap.dto;

import java.util.ArrayList;
import java.util.List;

public class Stream {
    /**
     * An array of (lat,lng) floating point values.
     */
    private List<LatLng> data;

    public List<LatLng> getData() {
        return data;
    }

    public void setData(List<List<Double>> rawData) {
        data = new ArrayList<>(rawData.size());

        for(List<Double> latLngPair : rawData) {
            data.add(new LatLng(latLngPair.get(0), latLngPair.get(1)));
        }
    }
}
