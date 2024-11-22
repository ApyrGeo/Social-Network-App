package map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.controller.LogInController;
import map.domain.validators.PrietenieValidator;
import map.domain.validators.UtilizatorValidator;
import map.repository.database.PrietenieDbRepository;
import map.repository.database.UtilizatorDbRepository;
import map.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/log_in.fxml"));


        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        LogInController c = fxmlLoader.getController();
        c.setService(constructMainService());
        c.setScene(scene);

        stage.setResizable(false);
        stage.setTitle("Log in");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Service constructMainService() {
        Service s = new Service(
                new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/socialnetwork",
                        "postgres",
                        "Injection17_DROP_TABLE",
                        new UtilizatorValidator()),
                new PrietenieDbRepository("jdbc:postgresql://localhost:5432/socialnetwork",
                        "postgres",
                        "Injection17_DROP_TABLE",
                        new PrietenieValidator()));

        return s;
    }
}