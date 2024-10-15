package main.java.ubb.scs.map.domain;

import java.time.LocalDateTime;

public class Prietenie extends Entity<Tuple<Long,Long>>{
    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


}
