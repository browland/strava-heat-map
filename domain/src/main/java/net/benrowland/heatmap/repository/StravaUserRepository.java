package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.StravaUserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StravaUserRepository extends CrudRepository<StravaUserEntity, String> {
    StravaUserEntity findByUsername(final String username);
    List<StravaUserEntity> findBySyncRequired(final boolean syncRequired);
}
