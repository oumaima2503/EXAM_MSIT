package ma.xproce.mediaclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
	private String id;
	private String title;
	private String description;
	private String url;
	private int durationSeconds;
	private CreatorDto creator;

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getUrl() { return url; }
	public void setUrl(String url) { this.url = url; }

	public int getDurationSeconds() { return durationSeconds; }
	public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

	public CreatorDto getCreator() { return creator; }
	public void setCreator(CreatorDto creator) { this.creator = creator; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VideoDto videoDto = (VideoDto) o;
		return durationSeconds == videoDto.durationSeconds && Objects.equals(id, videoDto.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, durationSeconds);
	}
}