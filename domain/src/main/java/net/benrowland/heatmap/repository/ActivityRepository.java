package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.ActivityEntity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {
}
