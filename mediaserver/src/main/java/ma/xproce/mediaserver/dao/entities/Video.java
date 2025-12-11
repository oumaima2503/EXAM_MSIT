package ma.xproce.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@ToString
@Data

@AllArgsConstructor
public class Video {

	@Id
	private String id;


	private  String name;;

	private String url;

	@Column(length = 2000)
	private String description;


	private int durationSeconds;

	@ManyToOne
	@JoinColumn(name = "creator_id")
	private Creator creator;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(int durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		this.creator = creator;
	}


	public Video(String name, String url, String description, int durationSeconds, Creator creator) {
		this.name = name;
		this.url = url;
		this.description = description;
		this.durationSeconds = durationSeconds;
		this.creator = creator;
	}

	public Video() {
	}
}
