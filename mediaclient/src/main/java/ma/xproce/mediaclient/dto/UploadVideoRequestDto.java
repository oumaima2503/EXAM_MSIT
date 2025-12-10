package ma.xproce.mediaclient.dto;

public class UploadVideoRequestDto {
	private String title;
	private String description;
	private String url;
	private int durationSeconds;
	private CreatorDto creator;

	public UploadVideoRequestDto() {}

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
}