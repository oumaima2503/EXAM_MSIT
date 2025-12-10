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

    private Integer id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Video> videos;

    public String getName() { return this.name; }
    public void setName(String name) {
        this.name = name;
    }

}
