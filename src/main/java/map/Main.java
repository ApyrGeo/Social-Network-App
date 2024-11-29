package map;


public class Main {
    public static void main(String[] args) {
        try{
            HelloApplication.main(args);
        }catch(Exception e){
            e.printStackTrace();
        }


//        Service s = new Service(
//                new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/socialnetwork",
//                        "postgres",
//                        "Injection17_DROP_TABLE",
//                        new UtilizatorValidator()),
//                new PrietenieDbRepository("jdbc:postgresql://localhost:5432/socialnetwork",
//                        "postgres",
//                        "Injection17_DROP_TABLE",
//                        new PrietenieValidator()));
//        new UI(s).Run();


    }
}