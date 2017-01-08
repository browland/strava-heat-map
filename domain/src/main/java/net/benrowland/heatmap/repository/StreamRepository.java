package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.StreamEntity;
import org.springframework.data.repository.CrudRepository;

public interface StreamRepository extends CrudRepository<StreamEntity, Long> {
}
