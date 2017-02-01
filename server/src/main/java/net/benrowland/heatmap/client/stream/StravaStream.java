package net.benrowland.heatmap.client.stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaStream {
    private String type;
    private List<List<Double>> data;

    public List<List<Double>> getData() {
        return data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StravaStream that = (StravaStream) o;
        return com.google.common.base.Objects.equal(type, that.type) &&
                com.google.common.base.Objects.equal(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, data);
    }

    @Override
    public String toString() {
        return "StravaStream{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}