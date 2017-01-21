package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.KmlEntity;
import org.springframework.data.repository.CrudRepository;

public interface KmlRepository extends CrudRepository<KmlEntity, String> {
    public KmlEntity findByStravaUsername(final String stravaUsername);
}
