package map.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Long>{
    @JsonProperty("id1")
    private Long idPrieten1;
    @JsonProperty("id2")
    private Long idPrieten2;

    private FriendshipStatus status;
    private LocalDateTime friendsFrom;

    private Prietenie() {}

    public Prietenie(Long id1, Long id2, LocalDateTime friendsFrom, FriendshipStatus status) {
        idPrieten1 = id1;
        idPrieten2 = id2;
        this.status = status;
        this.friendsFrom = friendsFrom;
    }

    public Long getIdPrieten1() {
        return idPrieten1;
    }
    public Long getIdPrieten2() {

        return idPrieten2;
    }
    public FriendshipStatus getStatus() {
        return status;
    }
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

}
