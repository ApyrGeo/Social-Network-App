package map.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UtilizatorPrietenieDTO {
    private String fullName;
    private String uname;
    private Long idUser1, idUser2;
    private LocalDateTime from;
    private String status;

    public UtilizatorPrietenieDTO(Utilizator u, Long idUser1, Long idUser2, LocalDateTime from, String status) {
        fullName = u.getFirstName() + " " + u.getLastName();
        uname = u.getUname();
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.from = from;
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }
    public String getUname() {
        return uname;
    }
    public Long getIdUser1() {
        return idUser1;
    }
    public Long getIdUser2() {
        return idUser2;
    }
    public LocalDateTime getFrom() {
        return from;
    }
    public String getStatus() {
        return status;
    }

}
