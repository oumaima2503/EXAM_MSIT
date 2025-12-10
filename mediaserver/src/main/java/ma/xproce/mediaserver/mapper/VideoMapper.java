package ma.xproce.mediaserver.mapper;


import ma.xproce.mediaserver.dao.entities.Video;
import ma.xproce.mediaserver.dto.VideoDto;
import ma.xproce.mediaserver.dto.VideoNewDto;

public class VideoMapper {

    public VideoDto fromVideoToVideoDto(Video video) {
        VideoDto videodto=new VideoDto();
        videodto.setName(video.getName());
        videodto.setDescription(video.getDescription());
        return videodto;

    }
    public Video fromVideoDtoToVideo(VideoDto videodto) {
        Video videod=new Video();
        videod.setName(videodto.getName());
        videod.setDescription(videodto.getDescription());
        return videod;
    }
    public Video fromVideoDtoNewtoVideo(VideoNewDto videodtonew) {
        Video videod=new Video();
        videod.setName(videodtonew.getName());
        videod.setDescription(videodtonew.getDescription());
        videod.setUrl(videodtonew.getUrl());
        return videod;
    }
}

