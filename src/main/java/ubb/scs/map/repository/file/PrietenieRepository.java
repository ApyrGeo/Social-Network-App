package ubb.scs.map.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.exceptions.RepositoryException;
import ubb.scs.map.domain.validators.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrietenieRepository extends AbstractFileRepository<Long, Prietenie> {
    

    public PrietenieRepository(Validator<Prietenie> validator, String fileName) throws IOException {
        super(validator, fileName);
    }

    @Override
    public List<Prietenie> createEntities() {
        try{
            Prietenie[] p = new ObjectMapper().readValue(new File(super.filename), new TypeReference<>() {});
            return Arrays.asList(p);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }
    /**
     * FUnction used for auto incrementing
     * @return the biggest id
     */
    public Long getLastId() {
        Long result = -1L;
        for(Long e : entities.keySet()){
            if( e > result ){
                result = e;
            }
        }
        return result;
    }

//    @Override
//    public JSONObject saveEntity(Prietenie entity) {
//        JSONObject result = new JSONObject();
//        result.put("id1", entity.getId().getLeft());
//        result.put("id2", entity.getId().getRight());
//        return result;
//    }
}
