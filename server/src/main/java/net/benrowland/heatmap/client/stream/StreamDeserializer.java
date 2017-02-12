package net.benrowland.heatmap.client.stream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Using a custom deserializer because the 'data' attribute can be either an array of lat/long pairs, or an array of
 * distance values (which we don't even want to keep).
 */
public class StreamDeserializer extends JsonDeserializer<StravaStream> {

    @Override
    public StravaStream deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        if("latlng".equals(node.get("type").textValue())) {
            List<List<Double>> data = new ArrayList<>();
            Iterator<JsonNode> latLngNodes = node.get("data").iterator();
            while(latLngNodes.hasNext()) {
                JsonNode latLngNode = latLngNodes.next();
                double lat = latLngNode.get(0).asDouble();
                double lng = latLngNode.get(1).asDouble();

                data.add(Arrays.asList(lat, lng));
            }
            StravaStream stravaStream = new StravaStream();
            stravaStream.setType("latlng");
            stravaStream.setData(data);
            return stravaStream;
        }
        else {
            return null;
        }
    }
}
