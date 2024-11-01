package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.UtilizatorExtended;
import ubb.scs.map.domain.exceptions.ServiceException;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.utils.Graph;

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

    /**
     * Tries to remove a user from the repo list.
     * It also deletes all the friendships containing this user
     * @param id - id of the user to be deleted
     * @throws ServiceException if the user is not found
     */
    public void removeUtilizator(Long id) {
        if(utilizatorRepository.delete(id).isEmpty())
            throw new ServiceException("Id not found");

        prietenieRepository.findAll().forEach(p -> {
            if(Objects.equals(p.getIdPrieten1(), id) || Objects.equals(p.getIdPrieten2(), id))

                prietenieRepository.delete(p.getId());
        });
    }

    /**
     * Tries to add a friendship to repo friendships
     * @param id1 - id of the first user
     * @param id2 - id of the second user
     * @throws ServiceException if one of the user is not found via the id or if the friendship already exists
     */
    public void addPrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1).isEmpty())
            throw new ServiceException("First id not found");

        if (utilizatorRepository.findOne(id2).isEmpty())
            throw new ServiceException("Second id not found");

        prietenieRepository.findAll().forEach(p -> {
            if(Objects.equals(p.getIdPrieten1(), id2) && Objects.equals(p.getIdPrieten2(), id1))
                throw new ServiceException("Prietenia exista deja");
            if(Objects.equals(p.getIdPrieten1(), id1) && Objects.equals(p.getIdPrieten2(), id2))
                throw new ServiceException("Prietenia exista deja");
        });

        Prietenie p = new Prietenie(id1, id2);
        p.setId(prietenieRepository.getLastId() + 1);
        prietenieRepository.save(p);
    }

    /**
     * Tries to remove a friendship from repo friendships
     * @param id - id of the friendship to be deleted
     * @throws ServiceException if the friendship does not exist
     */
    public void removePrietenie(Long id) {
        if(prietenieRepository.delete(id).isEmpty())
            throw new ServiceException("Id not found");
    }

    /**
     * Gets the user with the given id alongside it`s friends list
     * @param id - id of the wanted friend
     * @return user
     * @throws ServiceException if the user does not exist
     */
    public UtilizatorExtended getUtilizator(Long id) {
        if(utilizatorRepository.findOne(id).isEmpty())
            throw new ServiceException("Id not found");

        List<Utilizator> prieteni = new ArrayList<>();

        prietenieRepository.findAll().forEach(p -> {

            if(Objects.equals(id, p.getIdPrieten2())) {
                if (utilizatorRepository.findOne(p.getIdPrieten1()).isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(utilizatorRepository.findOne(p.getIdPrieten1()).get());
            }
            else if (Objects.equals(id, p.getIdPrieten1())) {
                if (utilizatorRepository.findOne(p.getIdPrieten2()).isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(utilizatorRepository.findOne(p.getIdPrieten2()).get());
            }
        });

        return new UtilizatorExtended(utilizatorRepository.findOne(id), prieteni);
    }

    /**
     * Gets all the users alongside their friends list
     * @return list with all the users
     */
    public List<UtilizatorExtended> getUtilizatori() {
        List<UtilizatorExtended> to_return = new ArrayList<>();

        utilizatorRepository.findAll().forEach(p -> {
            if (utilizatorRepository.findOne(p.getId()).isEmpty())
                throw new ServiceException("Id not found");

            Utilizator u = utilizatorRepository.findOne(p.getId()).get();
            to_return.add(getUtilizator(u.getId()));
        });

        return to_return;
    }

    /**
     * Gets all the friendships from the repo.
     * Each friend id is mapped into it`s full name
     * @return list of friendships in the format: 'id : friend1.lname -- friend2.lname'
     */
    public List<String> getPrietenii(){
        List<String> to_return = new ArrayList<>();
        prietenieRepository.findAll().forEach(p -> {
            if (utilizatorRepository.findOne(p.getIdPrieten1()).isEmpty() || utilizatorRepository.findOne(p.getIdPrieten2()).isEmpty())
                throw new ServiceException("Prietenia nu exista");

            to_return.add(p.getId().toString()
                    .concat(" : ")
                    .concat(utilizatorRepository.findOne(p.getIdPrieten1()).get().getLastName())
                    .concat(" <--> ")
                    .concat(utilizatorRepository.findOne(p.getIdPrieten2()).get().getLastName()));
        });
        return to_return;
    }

    /**
     * Calculates the longest friends group from all friendships
     * @return list containing the id`s of the users from the biggest community
     */
    public List<String> getLongestRelation(){
        Map<Long, ArrayList<Long>> nodes = createNodesMap();
        Map<Long, Integer> visited;

        List<Long> to_return = new ArrayList<>();
        for(Long k : nodes.keySet()){
            //reset visited map for each BFS
            visited = createVisitedMap();

            List<Long> currentSearch = new Graph().BFS(nodes, visited, k);

            //check if the BFS algorithm`s returned list is bigger than the
            //past maximum list
            if(currentSearch.size() > to_return.size()){
                to_return = currentSearch;
            }
        }

        //maps all the id`s into a String containing the fname and lname of the user with that id
        return to_return.stream()
                .map(id -> utilizatorRepository.findOne(id).get().getFirstName()
                        .concat(" ")
                        .concat(utilizatorRepository.findOne(id).get().getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the number of communities from all friendships
     * @return map containing the id`s of every user as a key and the community he`s part of as a value.
     * In this case, the number of communities can be calculated as the count of the distinct values from the map
     */
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

    /**
     * Creates a map that represents the Adjacent Nodes List of the friendships
     * @return map containing the id as a key and an array of 'adjacency' as value
     */
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

    /**
     * Creates an empty map that represents the visited nodes.
     * @return map containing id as a key and the visited status(default 0) as a value
     */
    private Map<Long, Integer>  createVisitedMap(){
        Map<Long, Integer> visited = new HashMap<>();
        utilizatorRepository.findAll().forEach(user -> {
            Long id = user.getId();
            visited.put(id, 0);
        });
        return visited;
    }
}
