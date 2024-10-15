package main.java.ubb.scs.map.repository.file;

import main.java.ubb.scs.map.domain.Utilizator;
import main.java.ubb.scs.map.domain.validators.Validator;
import main.java.ubb.scs.map.repository.file.AbstractFileRepository;

import java.io.BufferedWriter;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator> {
    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Utilizator createEntity(String line) {
        String[] splited = line.split(";");
        Utilizator u = new Utilizator(splited[1], splited[2]);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
        String s = entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
        return s;
    }
}
