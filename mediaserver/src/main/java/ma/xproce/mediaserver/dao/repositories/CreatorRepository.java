package ma.xproce.mediaserver.dao.repositories;

import ma.xproce.mediaserver.dao.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends
        JpaRepository<Creator, Integer> {
}