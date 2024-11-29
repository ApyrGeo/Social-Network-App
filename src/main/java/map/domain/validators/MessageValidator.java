package map.domain.validators;

import map.domain.Message;
import map.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getMessage().isEmpty())
            throw new ValidationException("Message is empty");

        if(entity.getFrom() < 0)
            throw new ValidationException("Invalid id");
    }
}
