package ubb.scs.map;


import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.service.Service;
import ubb.scs.map.ui.UI;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        Service s = new Service(
                new UtilizatorRepository(new UtilizatorValidator(), "src/main/java/ubb/scs/map/data/utilizatori.json"),
                new PrietenieRepository(new PrietenieValidator(), "src/main/java/ubb/scs/map/data/prietenii.json"));
        new UI(s).Run();

    }
}