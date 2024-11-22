package map.ui;

import map.service.Service;

//import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

public class UI {
    private final Service service;
    private final BufferedReader reader;

    public UI(Service service) {
        this.service = service;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void WriteMessage(String text, Colors color) {
        switch (color) {
            case GREEN :
            {
                System.out.println("\u001B[32m");
                break;
            }
            case RED:
            {
                System.out.println("\u001B[31m");
                break;
            }
        }
        System.out.println(text);
        System.out.println("\u001B[0m");
    }

    private void PrintMenuAddUser() throws IOException {
        System.out.println("Introdu prenumele: ");
        String fname = reader.readLine();
        System.out.println("Introdu numele: ");
        String lname = reader.readLine();
        System.out.println("Introdu uname: ");
        String uname = reader.readLine();

        service.addUtilizator(fname, lname, uname, "default");
        WriteMessage("Utilizator adaugat cu succes!", Colors.GREEN);
    }

    private void PrintMainMenu(){
        System.out.flush();
        System.out.println(
                "1: Adauga utilizator\n" +
                "2: Adauga prietenie\n" +
                "3: Sterge utilizator\n" +
                "4: Sterge prietenie\n\n" +
                "5: Afiseaza utilizatori\n" +
                "6: Afiseaza prietenii\n" +
                "7: Afiseaza numar de comunitati\n" +
                "8: Afiseaza cea mai sociabila comunitate\n\n" +
                "0: Iesire");

        System.out.println("Alege ce vrei sa faci: ");
    }

    public void Run(){

        while(true){
            try{
                PrintMainMenu();
                int choice = Integer.parseInt(reader.readLine());
                switch (choice){
                    case 0: return;
                    case 1: {PrintMenuAddUser();break;}
                    case 2: {PrintMenuAddPrietenie();break;}
                    case 3: {PrintMenuDeleteUser();break;}
                    case 4: {PrintMenuDeletePrietenie();break;}
                    case 5: {PrintUtilizatori();break;}
                    case 6: {PrintPrietenii();break;}
                    case 7: {PrintComunitati();break;}
                    case 8: {PrintSocietate();break;}
                }
            } catch (RuntimeException | IOException e) {
                WriteMessage(e.getMessage(), Colors.RED);
            }
        }
    }

    private void PrintPrietenii() {
        System.out.println("\n\n");
        System.out.println("Prietenii: ");
        service.getPrietenii().forEach(System.out::println);
        System.out.println("\n\n");
    }

    private void PrintSocietate() {
        System.out.println("\n\n");
        System.out.println("Cea mai sociabila comunitate: \n" +
                "Lungime: " + service.getLongestRelation().size()+
                "\nUtilizatori: \n" );
        service.getLongestRelation().forEach(System.out::println);
        System.out.println("\n\n");
    }

    private void PrintComunitati() {
        System.out.println("\n\n");
        System.out.println(
                "Numar comunitati: " +
                service.getRelations().values().stream().distinct().count()
        );
        System.out.println("\nRepartizare utilizatori: ");
        service.getRelations().forEach((k,v) -> {
            String result = "";
            result += service.getUtilizator(k).getLastName() + ": ";
            result += "comunitatea: " + v;
            System.out.println(result);
        });
        System.out.println("\n\n");
    }

    private void PrintMenuDeletePrietenie() throws IOException {
        System.out.println("\n\n");
        service.getPrietenii().forEach(System.out::println);
        System.out.println("Introdu id-ul prieteniei pe care vrei sa il stergi: ");
        Long id = Long.valueOf(reader.readLine());

        service.removePrietenie(id);
        WriteMessage("Prietenie stearsa cu succes!", Colors.GREEN);
        System.out.println("\n\n");
    }

    private void PrintMenuDeleteUser() throws IOException {
        System.out.println("\n\n");
        service.getUtilizatori().forEach(System.out::println);
        System.out.println("Introdu id-ul utilizatorului pe care vrei sa il stergi: ");
        Long id = Long.valueOf(reader.readLine());

        service.removeUtilizator(id);
        WriteMessage("Utilizator sters cu succes!", Colors.GREEN);
        System.out.println("\n\n");
    }

    private void PrintMenuAddPrietenie() throws IOException {
        System.out.println("\n\n");
        service.getUtilizatori().forEach(System.out::println);
        System.out.println("Selecteaza id-ul primului utilizator: ");
        Long id1 = Long.valueOf(reader.readLine());

        System.out.println("Selecteaza id-ul celui de-al doilea utilizator: ");
        Long id2 = Long.valueOf(reader.readLine());

        service.addPrietenie(id1, id2, LocalDateTime.now(), null);
        WriteMessage("Prietenie adaugata cu succes!", Colors.GREEN);
        System.out.println("\n\n");
    }

    private void PrintUtilizatori() {
        System.out.println("\n\n");
        System.out.println("Utilizatori: ");
        service.getUtilizatori().forEach(System.out::println);
        System.out.println("\n\n");
    }
}
