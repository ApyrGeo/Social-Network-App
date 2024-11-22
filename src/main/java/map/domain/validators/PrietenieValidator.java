package map.domain.validators;

import map.domain.Prietenie;
import map.domain.exceptions.ValidationException;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getIdPrieten1() < 0 || entity.getIdPrieten2() < 0)
            throw new ValidationException("Id-ul prieteniei trebuie sa fie un numar pozitiv!");
    }
}
