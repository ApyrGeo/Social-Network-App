package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.UtilizatorExtended;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.utils.Graph;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private final UtilizatorRepository utilizatorRepository;
    private final PrietenieRepository prietenieRepository;

    public Service(UtilizatorRepository utilizatorRepository, PrietenieRepository prietenieRepository) {
        this.utilizatorRepository = utilizatorRepository;
        this.prietenieRepository = prietenieRepository;
    }

    /**
     * Tries to add a new user to repo user
     * @param fname first name of user
     * @param lname last name of user
     */
    public void addUtilizator(String fname, String lname) {
        Utilizator utilizator = new Utilizator(fname, lname);
        utilizator.setId(utilizatorRepository.getLastId() + 1);
        utilizatorRepository.save(utilizator);
    }

    public void removeUtilizator(Long id) {
        if(utilizatorRepository.delete(id) == null)
            throw new IllegalArgumentException("Id not found");

        prietenieRepository.findAll().forEach(p -> {
            if(Objects.equals(p.getIdPrieten1(), id) || Objects.equals(p.getIdPrieten2(), id))
                prietenieRepository.delete(p.getId());
        });
    }

    public void addPrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1) == null)
            throw new IllegalArgumentException("First id not found");

        if (utilizatorRepository.findOne(id2) == null)
            throw new IllegalArgumentException("Second id not found");

        prietenieRepository.findAll().forEach(p -> {
            if(Objects.equals(p.getIdPrieten1(), id2) && Objects.equals(p.getIdPrieten2(), id1))
                throw new IllegalArgumentException("Prietenia exista deja");
        });

        Prietenie p = new Prietenie(id1, id2);
        p.setId(prietenieRepository.getLastId() + 1);
        prietenieRepository.save(p);
    }

    public void removePrietenie(Long id1, Long id2) {
        prietenieRepository.findAll().forEach(p -> {
            if(p.getIdPrieten1().equals(id1) && Objects.equals(p.getIdPrieten2(), id2))
                prietenieRepository.delete(p.getId());
        });
    }

    public UtilizatorExtended getUtilizator(Long id) {
        List<Utilizator> prieteni = new ArrayList<>();

        prietenieRepository.findAll().forEach(p -> {
            if(Objects.equals(id, p.getIdPrieten2()))
                prieteni.add(utilizatorRepository.findOne(p.getIdPrieten1()));
            else if (Objects.equals(id, p.getIdPrieten1()))
                prieteni.add(utilizatorRepository.findOne(p.getIdPrieten2()));

        });

        return new UtilizatorExtended(utilizatorRepository.findOne(id), prieteni);
    }

    public List<UtilizatorExtended> getUtilizatori() {
        List<UtilizatorExtended> to_return = new ArrayList<>();

        utilizatorRepository.findAll().forEach(p -> {
            Utilizator u = utilizatorRepository.findOne(p.getId());
            to_return.add(getUtilizator(u.getId()));
        });

        return to_return;
    }
    public List<String> getLongestRelation(){
        Map<Long, ArrayList<Long>> nodes = createNodesMap();
        Map<Long, Integer> visited;

        List<Long> to_return = new ArrayList<>();
        for(Long k : nodes.keySet()){
            visited = createVisitedMap();
            List<Long> currentSearch = new Graph().BFS(nodes, visited, k);
            if(currentSearch.size() > to_return.size()){
                to_return = currentSearch;
            }
        }

        return to_return.stream()
                .map(id -> utilizatorRepository.findOne(id).getFirstName()
                        .concat(" ")
                        .concat(utilizatorRepository.findOne(id).getLastName()))
                .collect(Collectors.toList());
    }

    public Map<Long, Integer> getRelations(){
       Map<Long, ArrayList<Long>> nodes = createNodesMap();
       Map<Long, Integer> visited = createVisitedMap();

       int count = 0;
       for(Long node : visited.keySet()) {
           if(visited.get(node) == 0) {
               count += 1;
               new Graph().DFS(nodes, visited, node, count);
           }
       }
        return visited;
    }

    private Map<Long, ArrayList<Long>> createNodesMap(){
        Map<Long, ArrayList<Long>> nodes = new HashMap<>();

        utilizatorRepository.findAll().forEach(user -> {
            nodes.put(user.getId(), new ArrayList<>());
        });

        for(Prietenie p : prietenieRepository.findAll()){
            ArrayList<Long> currentNodes = nodes.get(p.getIdPrieten1());
            currentNodes.add(p.getIdPrieten2());
            nodes.put(p.getIdPrieten1(), currentNodes);

            currentNodes = nodes.get(p.getIdPrieten2());
            currentNodes.add(p.getIdPrieten1());
            nodes.put(p.getIdPrieten2(), currentNodes);
        }
        return nodes;
    }
    private Map<Long, Integer>  createVisitedMap(){
        Map<Long, Integer> visited = new HashMap<>();
        utilizatorRepository.findAll().forEach(user -> {
            Long id = user.getId();
            visited.put(id, 0);
        });
        return visited;
    }
}
