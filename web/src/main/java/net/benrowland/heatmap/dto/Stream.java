package net.benrowland.heatmap.dto;

import com.google.common.base.Objects;

import java.util.Arrays;

public class Stream {
    private String type;
    private LatLng[] data;

    public LatLng[] getData() {
        return data;
    }

    public void setData(LatLng[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return Objects.equal(type, stream.type) &&
                Objects.equal(data, stream.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, data);
    }

    @Override
    public String toString() {
        return "Stream{" +
                "type='" + type + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
