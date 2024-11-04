package map.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Long>{
    @JsonProperty("id1")
    private Long idPrieten1;
    @JsonProperty("id2")
    private Long idPrieten2;

    private Prietenie() {}

    public Prietenie(Long id1, Long id2) {
        idPrieten1 = id1;
        idPrieten2 = id2;
    }

    public Long getIdPrieten1() {
        return idPrieten1;
    }
    public Long getIdPrieten2() {
        return idPrieten2;
    }


}
