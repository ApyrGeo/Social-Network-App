package map.repository.memory;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.exceptions.RepositoryException;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private final Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null)
            throw new RepositoryException("ID este null");

        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) throws ValidationException {
        if(entity==null)
            throw new RepositoryException("ENTITY CANNOT BE NULL");

        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null)
            throw new RepositoryException("ID CANNOT BE NULL");

        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new RepositoryException("ENTITY CANNOT BE NULL");

        validator.validate(entity);
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }
}
