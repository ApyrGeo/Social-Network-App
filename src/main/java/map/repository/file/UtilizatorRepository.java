package map.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.JSONObject;
import map.domain.Utilizator;
import map.domain.exceptions.RepositoryException;
import map.domain.validators.Validator;
import map.repository.file.AbstractFileRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator> {
    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public List<Utilizator> createEntities() {
        try{
            return new ObjectMapper().readValue(new File(super.filename), new TypeReference<List<Utilizator>>() {});
        }catch (IOException e){
            throw new RepositoryException(e);
        }
    }

    /**
     * FUnction used for auto incrementing
     * @return the biggest id
     */
    public Long getLastId(){
        Long result = -1L;
        for(Long e : entities.keySet()){
            if( e > result ){
                result = e;
            }
        }

        return result;
    }

//   public String saveEntity(Utilizator entity) {
//        String s = entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
//        return s;
//    }

//    @Override
//    public JSONObject saveEntity(Utilizator entity) {
//        JSONObject result = new JSONObject();
//        result.put("id", entity.getId());
//        result.put("first_name", entity.getFirstName());
//        result.put("last_name", entity.getLastName());
//
//        return result;
//    }

//    @Override
//    public Utilizator createEntity(String line) {
//        String[] splited = line.split(";");
//        Utilizator u = new Utilizator(splited[1], splited[2]);
//        u.setId(Long.parseLong(splited[0]));
//        return u;
//    }
}
