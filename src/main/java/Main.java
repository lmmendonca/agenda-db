import controller.ContactController;
import helper.FileHelper;
import model.Contact;
import model.Group;
import model.Phone;
import service.DataService;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String MENU_PATH = "src/main/resources/templates/menu.txt";

    public static void main(String[] args) throws IOException {
        DataService.createTeble();
        Scanner scanner = new Scanner(System.in);
        String comando = "";

        FileHelper.printFromFile(MENU_PATH);

        do {
            try {
                comando = scanner.nextLine();

                switch (comando) {
                    case "1":
                        new ContactController().getAll().forEach(System.out::println);
                        break;
                    case "2":
                        // add contato(pode ter grupo e telefones)
                        comando = "";
                        break;
                    case "3":
                        // edita contato(pode ter grupo e telefones)
                        break;
                    case "4":
                        // remove contato(pode ter grupo e telefones)
                        break;
                    case "5":
                        // limpar agenda
                        break;
                    case "8":
                        FileHelper.printFromFile(MENU_PATH);
                        break;
                    case "9":
                        System.out.println("Programa finalizado");
                        break;
                    default:
                        System.out.println("Comando não encontrado.");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (!comando.equals("9"));

    }

//    private static void addContato(Scanner scanner) {
//        List<String> fones = new ArrayList<>();
//        List<String> fones = new ArrayList<>();
//
//
//        System.out.println("Informe o Nome:");
//        String nome = scanner.nextLine();
//
//        System.out.println("Informe um Telefone:");
//        fones.add(scanner.nextLine());
//
//        do {
//            System.out.println("Infome mais um Telefone, caso não deseje, apenas clique ENTER.");
//            l = scanner.nextLine();
//            if (l.length() != 0) {
//                fones.add(l);
//            }
//        } while (l.length() != 0);
//
//        System.out.println(agenda.addContato(new Contato(agenda.nextId(), nome, fones)));
//    }

}
