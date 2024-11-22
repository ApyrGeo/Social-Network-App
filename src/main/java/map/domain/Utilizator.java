package map.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    @JsonProperty("first_name")
    protected String firstName;
    @JsonProperty("last_name")
    protected String lastName;

    protected String uname;

    protected String password;

    private Utilizator(){}

    public Utilizator(String firstName, String lastName, String uname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uname = uname;
        this.password = null;
    }
    public Utilizator(String firstName, String lastName, String uname, String pass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uname = uname;
        this.password = pass;
    }
    public String getUname() {
        return uname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    public String getPassword() {
        return password;
    }
}