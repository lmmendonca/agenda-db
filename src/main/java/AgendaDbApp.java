import controller.ContactController;
import helper.FileHelper;
import model.Contact;
import model.Group;
import model.Phone;
import service.DataService;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AgendaDbApp {
  private static final String MENU_PATH = "src/main/resources/templates/menu.txt";
  private static final Connection CONNECTION = DataService.CONNECTION;

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
            new ContactController(CONNECTION).getAll().forEach(System.out::println);
            break;
          case "2":
            addContact(scanner);
            break;
          case "3":
            // edita contato(pode ter grupo e telefones)
            System.out.println("Comando não Implementado!");
            break;
          case "4":
            deleteContact(scanner);
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

  private static void addContact(Scanner s) {
    Contact c = new Contact();

    System.out.println("Informe o Primeiro Nome:");
    c.setFirstName(s.nextLine());

    System.out.println("Informe o Sobrenome:");
    c.setLastName(s.nextLine());

    System.out.println("Informe o Email:");
    c.setEmail(s.nextLine());

    System.out.println("Gostaria de adicionar Telefone? (S/Outra tecla)");
    if (s.nextLine().toUpperCase().equals("S")) {
      c.setPhones(interatorPhone(s));
    }

    System.out.println("Gostaria de adicionar Grupo? (S/Outra tecla)");
    if (s.nextLine().toUpperCase().equals("S")) {
      c.setGroups(interatorGroup(s));
    }

    new ContactController(CONNECTION).create(c);
    System.out.println("Contato cadastrado com Sucesso!");
  }

  private static void editContact(Scanner s) {}

  private static void deleteContact(Scanner s) throws IOException {
    String DELETE_TXT = "src/main/resources/templates/delete.txt";
    FileHelper.printFromFile(DELETE_TXT);

    switch (s.nextLine()) {
      case "1":
        deleteByNome(s);
        break;
      case "2":
        deleteById(s);
        break;
    }
  }

  private static void deleteByNome(Scanner s) {
    System.out.println("Informe o nome Completo do Contato");
    Contact c = new ContactController(CONNECTION).getContactByName(s.nextLine());

    if (!(c == null)) {
      new ContactController(CONNECTION).delete(c);
      System.out.println("Contato Deletado com sucesso!");
    } else {
      System.out.println("Contato não encontrado");
    }
  }

  private static void deleteById(Scanner s) {
    System.out.println("Informe o ID Contato");
    Contact c = new ContactController(CONNECTION).getContactById(s.nextInt());

    if (!(c == null)) {
      new ContactController(CONNECTION).delete(c);
    } else {
      System.out.println("Contato não encontrado");
    }
  }

  private static List<Phone> interatorPhone(Scanner s) {
    List<Phone> phones = new ArrayList<>();
    boolean add = true;

    do {
      phones.add(addPhone(s));

      System.out.println("Gortaria de adicionar mais um Telefone? (S/Outra tecla)");

      if (!s.nextLine().toUpperCase().equals("S")) add = false;

    } while (add);

    return phones;
  }

  private static List<Group> interatorGroup(Scanner s) {
    List<Group> groups = new ArrayList<>();
    boolean add = true;

    do {
      groups.add(addGroup(s));

      System.out.println("Gortaria de adicionar mais um Grupo? (S/Outra tecla)");

      if (!s.nextLine().toUpperCase().equals("S")) add = false;

    } while (add);

    return groups;
  }

  private static Phone addPhone(Scanner s) {
    System.out.println("Informe o Telefone");
    return new Phone(s.nextLine());
  }

  private static Group addGroup(Scanner s) {
    System.out.println("Informe a Descricao do Grupo");
    return new Group(s.nextLine());
  }
}
