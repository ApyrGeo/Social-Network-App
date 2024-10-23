package ubb.scs.map;


import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.service.Service;


public class Main {
    public static void main(String[] args) {

//        Repository<Long, Utilizator> repo = new InMemoryRepository<Long, Utilizator>(new UtilizatorValidator());
//        Repository<Long, Utilizator> repoFile = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");
//        Utilizator u1 = new Utilizator("IONUT", "a");
//        Utilizator u2 = new Utilizator("Mihai", "b");
//        Utilizator u3 = null;
//        u1.setId(1L);
//        u2.setId(2L);
//        try {
//            repoFile.save(u1);
//            repoFile.save(u2);
//
//        }catch(IllegalArgumentException e)
//        {
//            System.out.println(e.getMessage());
//        }catch(ValidationException e)
//        {
//            System.out.println(e.getMessage());
//        }
//        System.out.println();


//        UtilizatorDbRepository udbr = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","Injection17_DROP_TABLE");
//
//        udbr.save(new Utilizator("TST", "STS"));
//
//        for(Utilizator u : udbr.findAll()){
//            System.out.println(u);
//        }


        Service s = new Service(
                new UtilizatorRepository(new UtilizatorValidator(), "src/main/java/ubb/scs/map/data/utilizatori.json"),
                new PrietenieRepository(new PrietenieValidator(), "src/main/java/ubb/scs/map/data/prietenii.json"));

        try {
//            s.addUtilizator("A", "B");
//            s.addUtilizator("A", "B");
//            s.addUtilizator("A", "B");
//            s.addUtilizator("A", "B");
//            s.addUtilizator("A", "B");
//            s.addPrietenie(0L, 1L);
//            s.addPrietenie(1L, 2L);
//            s.addPrietenie(4L, 1L);
//            s.addPrietenie(3L, 2L);
//            s.addPrietenie(0L, 2L);

            //System.out.println(s.getUtilizatori());
            System.out.println(s.getRelations().values().stream().distinct().count());

            System.out.println(s.getLongestRelation());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}