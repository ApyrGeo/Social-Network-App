package map.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.exceptions.RepositoryException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.List;
import java.util.Optional;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    protected final String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename=fileName;
        loadData();
    }

    /**
     * Function that maps the values from the .json format to the specific Object type
     * @return list of entities parsed from the file
     * @throws IOException if an error occurred
     */
    public abstract List<E> createEntities();
    //public abstract JSONObject saveEntity(E entity);
    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isEmpty())
            writeToFile();
        return e;
    }

    /**
     * Function that loads data from file, by creating the entities and saving each one in the repo list
     */
    private void loadData() {
        List<E> l = createEntities();
        l.forEach(super::save);
    }

    /**
     * Function that writes to file the repo entities
     */
    private void writeToFile() {
        try {
            new ObjectMapper().writeValue(new File(filename), entities.values());
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> deleted = super.delete(id);

        if(deleted.isPresent())
            writeToFile();

        return deleted;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> updated = super.update(entity);

        if(updated.isEmpty())
            writeToFile();

        return updated;
    }

//    private void writeToFile() {
//
//        try  (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
//            for (E entity: entities.values()) {
//                String ent = saveEntity(entity);
//                writer.write(ent);
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private void loadData(){
//        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
//            String line;
//            while((line = reader.readLine()) != null){
//                E entity = createEntity(line);
//                super.save(entity); // nu e this.save() pt ca nu vrem sa salvam in file in timp ce cititm din file
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}
