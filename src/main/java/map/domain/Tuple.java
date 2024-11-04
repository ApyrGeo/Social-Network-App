package map.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Tuple<E1, E2> {

    protected E1 e1;

    protected E2 e2;

    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 getLeft() {
        return e1;
    }
    public E2 getRight() {
        return e2;
    }
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return e1 + "," + e2;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null || this.getClass() != obj.getClass())
            return false;

        Tuple<E1,E2> newObj = (Tuple) obj;
        return e1.equals(newObj.getLeft()) && e2.equals(newObj.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
