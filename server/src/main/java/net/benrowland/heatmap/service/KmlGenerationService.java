package net.benrowland.heatmap.service;

import de.micromata.opengis.kml.v_2_2_0.Kml;
import net.benrowland.heatmap.entity.KmlEntity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import net.benrowland.heatmap.repository.KmlRepository;
import net.benrowland.heatmap.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class KmlGenerationService {

    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private KmlRepository kmlRepository;

    public void generateKmlDocument(StravaUserEntity stravaUserEntity) {
        List<StreamEntity> streams = streamRepository.findAllByStravaUsername(stravaUserEntity.getStravaUsername());

        Kml kml = new Kml();
        kml.createAndSetPlacemark()
                .withName("London, UK").withOpen(Boolean.TRUE)
                .createAndSetPoint().addToCoordinates(-0.126236, 51.500152);

        StringWriter writer = new StringWriter();
        kml.marshal(writer);

        KmlEntity kmlEntity = new KmlEntity();
        kmlEntity.setStravaUsername(stravaUserEntity.getStravaUsername());
        kmlEntity.setKmlDocument(writer.toString());

        kmlRepository.save(kmlEntity);
    }
}
