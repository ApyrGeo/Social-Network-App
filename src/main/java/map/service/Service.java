package map.service;

import map.domain.Prietenie;
import map.domain.Utilizator;
import map.domain.UtilizatorExtended;
import map.domain.exceptions.ServiceException;
import map.repository.database.PrietenieDbRepository;
import map.repository.database.UtilizatorDbRepository;
import map.utils.Graph;
import map.utils.events.ChangeEventType;
import map.utils.events.EntityChangeEvent;
import map.utils.observer.Observable;
import map.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service implements Observable<EntityChangeEvent> {
    private final UtilizatorDbRepository utilizatorRepository;
    private final PrietenieDbRepository prietenieRepository;

    private List<Observer<EntityChangeEvent>> observers;

    public Service(UtilizatorDbRepository utilizatorRepository, PrietenieDbRepository prietenieRepository) {
        this.utilizatorRepository = utilizatorRepository;
        this.prietenieRepository = prietenieRepository;
        observers = new ArrayList<>();
    }

    public Utilizator authentificate(String uname, String pass) {
        Optional<Utilizator> u = utilizatorRepository.findOne(uname, pass);
        if(u.isEmpty())
            throw new ServiceException("Invalid username or password");


        return u.get();
    }

    /**
     * Tries to add a new user to repo user
     * @param fname first name of user
     * @param lname last name of user
     */
    public void addUtilizator(String fname, String lname, String uname, String pass) {
        Utilizator utilizator = new Utilizator(fname, lname, uname, pass);
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
        if(utilizatorRepository.findOne(id).isEmpty())
            throw new ServiceException("Id not found");

//        prietenieRepository.findAll().forEach(p -> {
//            if(Objects.equals(p.getIdPrieten1(), id) || Objects.equals(p.getIdPrieten2(), id))
//                prietenieRepository.delete(p.getId());
//        });

        utilizatorRepository.delete(id);
    }

    /**
     * Tries to add a friendship to repo friendships
     * @param id1 - id of the first user
     * @param id2 - id of the second user
     * @throws ServiceException if one of the user is not found via the id or if the friendship already exists
     */
    public void addPrietenie(Long id1, Long id2, LocalDateTime from, String status) {
        if (utilizatorRepository.findOne(id1).isEmpty())
            throw new ServiceException("First id not found");

        if (utilizatorRepository.findOne(id2).isEmpty())
            throw new ServiceException("Second id not found");

        if (prietenieRepository.findOne(id1,id2).isPresent() ||
            prietenieRepository.findOne(id2,id1).isPresent())
            throw new ServiceException("Prietenia exista deja");

        Prietenie p = new Prietenie(id1, id2, from, status);
        p.setId(prietenieRepository.getLastId() + 1);
        prietenieRepository.save(p);

        notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, p));
    }

    /**
     * Tries to remove a friendship from repo friendships
     * @param id - id of the friendship to be deleted
     * @throws ServiceException if the friendship does not exist
     */
    public void removePrietenie(Long id) {
        Optional<Prietenie> removed = prietenieRepository.delete(id);

        if(removed.isEmpty())
            throw new ServiceException("Id not found");

        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, removed.get()));
    }

    /**
     * Gets the user with the given id alongside it`s friends list
     * @param id - id of the wanted friend
     * @return user
     * @throws ServiceException if the user does not exist
     */
    public UtilizatorExtended getUtilizator(Long id) {
        Optional<Utilizator> utilizator = utilizatorRepository.findOne(id);
        if(utilizator.isEmpty())
            throw new ServiceException("Id not found");

        List<Utilizator> prieteni = new ArrayList<>();

        prietenieRepository.findAll().forEach(p -> {

            if(Objects.equals(id, p.getIdPrieten2())) {
                Optional<Utilizator> friend1 = utilizatorRepository.findOne(p.getIdPrieten1());
                if (friend1.isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(friend1.get());
            }
            else if (Objects.equals(id, p.getIdPrieten1())) {
                Optional<Utilizator> friend2 = utilizatorRepository.findOne(p.getIdPrieten2());
                if (friend2.isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(friend2.get());
            }
        });

        return new UtilizatorExtended(utilizator, prieteni);
    }

    public UtilizatorExtended getUtilizator(String uname){
        Optional<Utilizator> utilizator = utilizatorRepository.findOne(uname);
        if(utilizator.isEmpty())
            throw new ServiceException("User not found");

        Long id = utilizator.get().getId();

        List<Utilizator> prieteni = new ArrayList<>();

        prietenieRepository.findAll().forEach(p -> {

            if(Objects.equals(id, p.getIdPrieten2())) {
                Optional<Utilizator> friend1 = utilizatorRepository.findOne(p.getIdPrieten1());
                if (friend1.isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(friend1.get());
            }
            else if (Objects.equals(id, p.getIdPrieten1())) {
                Optional<Utilizator> friend2 = utilizatorRepository.findOne(p.getIdPrieten2());
                if (friend2.isEmpty())
                    throw new ServiceException("Prietenia nu exista");
                prieteni.add(friend2.get());
            }
        });

        return new UtilizatorExtended(utilizator, prieteni);
    }

    /**
     * Gets all the users alongside their friends list
     * @return list with all the users
     */
    public List<UtilizatorExtended> getUtilizatori() {
        List<UtilizatorExtended> to_return = new ArrayList<>();

        utilizatorRepository.findAll().forEach(p -> {
            Optional<Utilizator> utilizator = utilizatorRepository.findOne(p.getId());
            if (utilizator.isEmpty())
                throw new ServiceException("Id not found");

            Utilizator u = utilizator.get();
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
            Optional<Utilizator> friend1 = utilizatorRepository.findOne(p.getIdPrieten1());
            Optional<Utilizator> friend2 = utilizatorRepository.findOne(p.getIdPrieten2());
            if (friend1.isEmpty() || friend2.isEmpty())
                throw new ServiceException("Prietenia nu exista");

            to_return.add(p.getId().toString()
                    .concat(" : ")
                    .concat(friend1.get().getLastName())
                    .concat(" <--> ")
                    .concat(friend2.get().getLastName()));
        });
        return to_return;
    }

    public Iterable<Prietenie> getFriendships() {
        return prietenieRepository.findAll();
    }

    public Prietenie getPrietenie(Long id1, Long id2) {
        Optional<Prietenie> p = prietenieRepository.findOne(id1, id2);
        if(p.isEmpty()) {
            p = prietenieRepository.findOne(id2, id1);
            if (p.isEmpty())
                throw new ServiceException("Prietenia nu exista");
        }
        return p.get();
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

    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent event) {
        observers.stream().forEach(x->x.update(event));
    }

    public void updatePrietenie(Long id, Long id1, Long id2, String newStatus) {
        Prietenie newP = new Prietenie(id1, id2, LocalDateTime.now(), newStatus);
        newP.setId(id);
        prietenieRepository.update(newP);

        notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, newP));
    }
}
