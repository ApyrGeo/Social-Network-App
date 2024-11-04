package map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorExtended extends Utilizator {
    private List<Utilizator> prieteni;
    public UtilizatorExtended(Optional<Utilizator> utilizator, List<Utilizator> prieteni) {
        super(utilizator.get().getFirstName(), utilizator.get().getLastName());
        super.setId(utilizator.get().getId());
        this.prieteni = prieteni;
    }
    public List<Utilizator> getPrieteni() {
        return prieteni;
    }
    public void setPrieteni(ArrayList<Utilizator> prieteni) {
        this.prieteni = prieteni;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id='" + getId() + '\'' +
                "firstName='" + super.firstName + '\'' +
                ", lastName='" + super.lastName + '\'' +
                ", friends=" + prieteni +
                '}';
    }
}
