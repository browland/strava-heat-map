package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.StravaUserEntity;
import org.springframework.data.repository.CrudRepository;

public interface StravaUserRepository extends CrudRepository<StravaUserEntity, String> {
    StravaUserEntity findByUsername(final String username);
}
