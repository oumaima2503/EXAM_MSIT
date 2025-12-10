package ma.xproce.mediaclient.dto;

import java.util.List;

public class VideoStreamDto {
	private List<VideoDto> videos;

	public VideoStreamDto() {}

	public VideoStreamDto(List<VideoDto> videos) { this.videos = videos; }

	public List<VideoDto> getVideos() { return videos; }
	public void setVideos(List<VideoDto> videos) { this.videos = videos; }
}