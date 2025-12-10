package ma.xproce.mediaclient.mapper;

import ma.xproce.mediaappgrpc.proto.Creator;
import ma.xproce.mediaappgrpc.proto.UploadVideoRequest;
import ma.xproce.mediaappgrpc.proto.Video;
import ma.xproce.mediaappgrpc.proto.VideoStream;
import ma.xproce.mediaclient.dto.CreatorDto;
import ma.xproce.mediaclient.dto.UploadVideoRequestDto;
import ma.xproce.mediaclient.dto.VideoDto;
import ma.xproce.mediaclient.dto.VideoStreamDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoMapper {

	// proto Video -> DTO
	public VideoDto fromVideoProtoToVideoDto(Video v) {
		if (v == null) return null;
		return new VideoDto(
				v.getId(),
				v.getTitle(),
				v.getDescription(),
				v.getUrl(),
				v.getDurationSeconds(),
				fromCreatorProtoToDto(v.hasCreator() ? v.getCreator() : null)
		);
	}

	// proto Creator -> DTO
	public CreatorDto fromCreatorProtoToDto(Creator c) {
		if (c == null) return null;
		return new CreatorDto(c.getId(), c.getName(), c.getEmail());
	}

	// DTO -> proto UploadVideoRequest
	public UploadVideoRequest fromUploadDtoToProto(UploadVideoRequestDto dto) {
		UploadVideoRequest.Builder b = UploadVideoRequest.newBuilder();
		if (dto == null) return b.build();
		if (dto.getTitle() != null) b.setTitle(dto.getTitle());
		if (dto.getDescription() != null) b.setDescription(dto.getDescription());
		if (dto.getUrl() != null) b.setUrl(dto.getUrl());
		b.setDurationSeconds(dto.getDurationSeconds());
		if (dto.getCreator() != null) {
			b.setCreator(Creator.newBuilder()
					.setId(dto.getCreator().getId() == null ? "" : dto.getCreator().getId())
					.setName(dto.getCreator().getName() == null ? "" : dto.getCreator().getName())
					.setEmail(dto.getCreator().getEmail() == null ? "" : dto.getCreator().getEmail())
					.build());
		}
		return b.build();
	}

	// proto VideoStream -> DTO
	public VideoStreamDto fromVideoStreamProtoToDto(VideoStream s) {
		if (s == null) return new VideoStreamDto();
		List<VideoDto> list = s.getVideosList().stream().map(this::fromVideoProtoToVideoDto).collect(Collectors.toList());
		return new VideoStreamDto(list);
	}
}