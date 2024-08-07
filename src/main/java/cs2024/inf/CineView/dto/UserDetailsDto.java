package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.List;

public class UserDetailsDto extends UserDto {

    private UUID id;

    private Set<UUID> followers;
    private Set<UUID> following;

    private List<FilmListDto> filmLists;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<UUID> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UUID> followers) {
        this.followers = followers;
    }

    public Set<UUID> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UUID> following) {
        this.following = following;
    }

    public List<FilmListDto> getFilmLists() {
        return filmLists;
    }

    public void setFilmLists(List<FilmListDto> filmLists) {
        this.filmLists = filmLists;
    }
}