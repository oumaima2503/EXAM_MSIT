package ma.xproce.mediaserver.dao.repositories;

import ma.xproce.mediaserver.dao.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends
        JpaRepository<Video, Integer> {
}
