package ma.xproce.mediaserver.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Video> videos;

    public String getName() { return this.name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
