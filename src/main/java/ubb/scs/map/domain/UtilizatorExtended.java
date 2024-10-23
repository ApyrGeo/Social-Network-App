package ubb.scs.map.domain;

import java.util.ArrayList;
import java.util.List;

public class UtilizatorExtended extends Utilizator {
    private List<Utilizator> prieteni;
    public UtilizatorExtended(Utilizator utilizator, List<Utilizator> prieteni) {
        super(utilizator.getFirstName(), utilizator.getLastName());
        super.setId(utilizator.getId());
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
                "firstName='" + super.firstName + '\'' +
                ", lastName='" + super.lastName + '\'' +
                ", friends=" + prieteni +
                '}';
    }
}
