package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.StreamEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StreamRepository extends CrudRepository<StreamEntity, Long> {

    public List<StreamEntity> findAllByStravaUsername(final String stravaUsername);
}
