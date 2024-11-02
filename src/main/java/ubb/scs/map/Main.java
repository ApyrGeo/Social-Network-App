package ubb.scs.map;


import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.service.Service;
import ubb.scs.map.ui.UI;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

        Service s = new Service(
                new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Injection17_DROP_TABLE"),
                new PrietenieDbRepository("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Injection17_DROP_TABLE"));
        new UI(s).Run();

    }
}