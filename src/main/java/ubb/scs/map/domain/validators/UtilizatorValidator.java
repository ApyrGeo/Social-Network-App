package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.exceptions.ValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName().isEmpty())
            throw new ValidationException("Prenume invalid");

        if(entity.getLastName().isEmpty())
            throw new ValidationException("Nume invalid");
    }
}
