package ma.xproce.mediaserver.mapper;

import ma.xproce.mediaappgrpc.proto.UploadVideoRequest;
import ma.xproce.mediaserver.dao.entities.Video;  // Entité Video
import ma.xproce.mediaserver.dao.entities.Creator; // Entité Creator
import org.springframework.stereotype.Component;

@Component
public class VideoMapper {

	private final CreatorMapper creatorMapper;

	public VideoMapper(CreatorMapper creatorMapper) {
		this.creatorMapper = creatorMapper;
	}

	// Proto Video to Video entity
	public Video toEntity(ma.xproce.mediaappgrpc.proto.Video proto) {
		if (proto == null) return null;
		Video entity = new Video();
		entity.setId(proto.getId());
		entity.setTitle(proto.getTitle());
		entity.setDescription(proto.getDescription());
		entity.setUrl(proto.getUrl());
		entity.setDurationSeconds(proto.getDurationSeconds());
		if (proto.hasCreator()) {
			entity.setCreator(creatorMapper.toEntity(proto.getCreator()));
		}
		return entity;
	}

	// Video entity to proto Video
	public ma.xproce.mediaappgrpc.proto.Video toProto(Video e) {
		if (e == null) return ma.xproce.mediaappgrpc.proto.Video.getDefaultInstance();
		ma.xproce.mediaappgrpc.proto.Video.Builder b = ma.xproce.mediaappgrpc.proto.Video.newBuilder()
				.setId(e.getId() == null ? "" : e.getId())
				.setTitle(e.getTitle() == null ? "" : e.getTitle())
				.setDescription(e.getDescription() == null ? "" : e.getDescription())
				.setUrl(e.getUrl() == null ? "" : e.getUrl())
				.setDurationSeconds(e.getDurationSeconds());
		if (e.getCreator() != null) {
			b.setCreator(creatorMapper.toProto(e.getCreator()));
		}
		return b.build();
	}
}