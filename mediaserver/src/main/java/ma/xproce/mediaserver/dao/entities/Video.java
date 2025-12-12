package ma.xproce.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
	// id aligné sur le proto (string UUID)
	@Id
	private String id;

	// titre (proto: title)
	private String title;

	private String url;

	@Column(length = 2000)
	private String description;

	// durée en secondes (proto: duration_seconds)
	private int durationSeconds;

	@ManyToOne
	@JoinColumn(name = "creator_id")
	private Creator creator;

	public String getId() {
		return id;
	}

	public Video() {
	}

	public Video(String id, String title, String url, int durationSeconds, String description, Creator creator) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.durationSeconds = durationSeconds;
		this.description = description;
		this.creator = creator;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	// constructeur pratique sans id (id sera généré côté service)
	public Video(String title, String url, String description, int durationSeconds, Creator creator) {
		this.title = title;
		this.url = url;
		this.description = description;
		this.durationSeconds = durationSeconds;
		this.creator = creator;
	}
}
