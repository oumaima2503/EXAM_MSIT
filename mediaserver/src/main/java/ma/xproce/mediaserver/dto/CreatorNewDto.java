package ma.xproce.mediaserver.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CreatorNewDto {

    private String id;
    private String name;
    private String email;


    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "CreatorDto{name='" + name + "', email='" + email + "'}";
    }
}