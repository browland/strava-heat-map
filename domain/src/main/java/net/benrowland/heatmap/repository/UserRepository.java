package net.benrowland.heatmap.repository;

import net.benrowland.heatmap.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
