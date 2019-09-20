import controller.ContactController;
import controller.GroupsController;
import controller.PhoneController;
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
              listagem();
            break;
          case "2":
            addContact(scanner);
            break;
          case "3":
            edit(scanner);
            break;
          case "4":
            delete(scanner);
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

    private static void listagem() {
        tableHeader();
        new ContactController(CONNECTION).getAll().forEach(AgendaDbApp::print);
    }

    private static void tableHeader() {
        System.out.format(
                "+-----+------------+---------------------------+---------------------------+------------------+---------------+%n");
        System.out.format(
                "| ID  | Nome       | Sobrenome                 | Email                     | Telefone         | Grupo         |%n");
        System.out.format(
                "+-----+------------+---------------------------+---------------------------+------------------+---------------+%n");
    }

    private static void print(Contact c) {
        if (c.getPhones().size() > 0) {

            if (c.getGroups().size() > 0) {

                for (Phone p : c.getPhones()) {
                    for (Group g : c.getGroups()) {
                        formatTable(c, p.formatPhone(), g.getDescription());
                    }
                }
                return;

            } else {
                for (Phone p : c.getPhones()) {
                    formatTable(c, p.formatPhone(), null);
                }
            }
        } else if (c.getGroups().size() > 0) {
            for (Group g : c.getGroups()) {
                formatTable(c, null, g.getDescription());
            }
        } else {
            formatTable(c, null, null);
        }
    }

    private static void formatTable(Contact c, String phone, String group) {
        String leftAlignFormat = "| %-3s | %-10s | %-25s | %-25s | %-15s | %-13s |%n";
        System.out.format(
                leftAlignFormat,
                c.getContactId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                phone == null ? "Sem Telefone" : phone,
                group == null ? "Sem Grupo" : group);
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
      tableHeader();
      print(c);
  }

  private static void edit(Scanner s) throws IOException {
    String BUSCAR_TXT = "src/main/resources/templates/buscar.txt";
    FileHelper.printFromFile(BUSCAR_TXT);

    switch (s.nextLine()) {
      case "1":
        editOptions(s, serchByName(s));
        break;
      case "2":
        editOptions(s, serchByID(s));
        break;
      default:
        System.out.println("Comando não encontrado");
        break;
    }
  }

  private static void editOptions(Scanner s, Contact c) throws IOException {
      if (c != null) {
          String EDICAO_TXT = "src/main/resources/templates/edicao/edicao.txt";
          FileHelper.printFromFile(EDICAO_TXT);

          switch (s.nextLine()) {
              case "1":
                  editContact(s, c);
                  break;
              case "2":
                  editPhone(s, c);
                  break;
              case "3":
                  editGroup(s, c);
                  break;
              default:
                  System.out.println("Comando não encontrado");
                  break;
          }
    }
  }

  private static void editContact(Scanner s, Contact c) throws IOException {
    String EDICAO_TXT = "src/main/resources/templates/edicao/contato.txt";
    FileHelper.printFromFile(EDICAO_TXT);

    switch (s.nextLine()) {
      case "1":
        System.out.println("Informe o novo Primeiro nome:");
        c.setFirstName(s.nextLine());
        new ContactController(CONNECTION).update(c);
        System.out.println("Contato editado com sucesso");
        break;
      case "2":
        System.out.println("Informe o novo Sobrenome");
        c.setLastName(s.nextLine());
        new ContactController(CONNECTION).update(c);
        System.out.println("Contato editado com sucesso");
        break;
      case "3":
        System.out.println("Informe o novo Email");
        c.setEmail(s.nextLine());
        new ContactController(CONNECTION).update(c);
        System.out.println("Contato editado com sucesso");
        break;
      default:
        System.out.println("Comando não encontrado");
        break;
    }
  }

    private static void editPhone(Scanner s, Contact c) throws IOException {

        System.out.println("Deseja editar um telefone existente? (S/ Outra tecla)");

        if (c.getPhones().size() == 0) {
            System.out.println("O contato não possui telefone cadastrado");
            return;
        }

        if (s.nextLine().equals("s")) {
            System.out.println("Informe o telefone que deseja alterar");
            String tel = s.nextLine();
            Phone p = new Phone();

            c.getPhones()
                    .forEach(
                            phone -> {
                                if (phone.getPhone().equals(tel)) {
                                    p.setPhoneId(phone.getPhoneId());
                                    p.setPhone(phone.getPhone());
                                }
                            });

            if (p.getPhoneId() == null) {
                System.out.println("Telefone não encontrado");
                return;
            }

            String EDICAO_TXT = "src/main/resources/templates/edicao/telefone.txt";
            FileHelper.printFromFile(EDICAO_TXT);

            switch (s.nextLine()) {
                case "1":
                    System.out.println("Informe o novo Telefone:");
                    p.setPhone(s.nextLine());
                    new PhoneController(CONNECTION).update(p);
                    System.out.println("Contato editado com sucesso");
                    break;
                case "2":
                    new ContactController(CONNECTION).removePhone(c, p);
                    System.out.println("Contato editado com sucesso");
                    break;
                default:
                    System.out.println("Comando não encontrado");
                    break;
            }
        }

        System.out.println("Deseja adicionar um novo Telefone ao contato? (S/ Outra tecla)");

        if (s.nextLine().toUpperCase().equals("S")) {
            new ContactController(CONNECTION)
                    .createContactsPhones(c,
                            new PhoneController(CONNECTION).create(interatorPhone(s)));
        }
    }

    private static void editGroup(Scanner s, Contact c) throws IOException {
    System.out.println("Deseja editar um grupo existente? (S/ Outra tecla)");

    if (s.nextLine().equals("s")) {
      System.out.println("Informe a Descrição do grupo que deseja alterar");
      String descricao = s.nextLine();
      Group grupo = new Group();

      c.getGroups()
          .forEach(
              g -> {
                if (g.getDescription().equals(descricao)) {
                  grupo.setGroupId(g.getGroupId());
                  grupo.setDescription(g.getDescription());
                }
              });

      if (grupo.getGroupId() == null) {
        System.out.println("Grupo não encontrado");
        return;
      }

      String EDICAO_TXT = "src/main/resources/templates/edicao/grupo.txt";
      FileHelper.printFromFile(EDICAO_TXT);

      switch (s.nextLine()) {
        case "1":
          System.out.println("Informe a nova descricao");
          grupo.setDescription(s.nextLine());
          new GroupsController(CONNECTION).update(grupo);
          System.out.println("Descrição do grupo alterada com sucesso!");
          break;
        case "2":
          new ContactController(CONNECTION).removeGroup(c, grupo);
          System.out.println("Grupo removido com Sucesso!");
          break;
        default:
          System.out.println("Comando não encontrado");
          break;
      }
    }

    System.out.println("Deseja adicionar um novo grupo ao contato? (S/ Outra tecla)");

    if (s.nextLine().toUpperCase().equals("S")) {
      new ContactController(CONNECTION)
          .createContactsGroups(c, new GroupsController(CONNECTION).create(interatorGroup(s)));
    }
  }

  private static void delete(Scanner s) throws IOException {
    String DELETE_TXT = "src/main/resources/templates/delete.txt";
    FileHelper.printFromFile(DELETE_TXT);

    switch (s.nextLine()) {
      case "1":
        deleteContact(serchByName(s));
        break;
      case "2":
        deleteContact(serchByID(s));
        break;
        default:
            System.out.println("Comando não encontrado");
            break;
    }
  }

  private static void deleteContact(Contact c) {
      if (c != null) {
          tableHeader();
          print(c);
      new ContactController(CONNECTION).delete(c);
      System.out.println("Contato Deletado com sucesso!");
    }
  }

  private static Contact serchByName(Scanner s) {
    System.out.println("Informe o nome Completo do Contato");
      Contact c = new ContactController(CONNECTION).getContactByName(s.nextLine());
      if (c == null) {
          System.out.println("Contato não encontrado");
          return null;
      }
      return c;
  }

  private static Contact serchByID(Scanner s) {
    System.out.println("Informe o ID Contato");
    String id = s.nextLine();
      Contact c = new ContactController(CONNECTION).getContactById(Integer.parseInt(id));
      if (c == null) {
          System.out.println("Contato não encontrado");
          return null;
      }
      return c;
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
    String tel = s.nextLine();
    Phone phone = new PhoneController(CONNECTION).getPhoneByPhone(tel);

    if (phone == null) return new Phone(tel);

    return phone;
  }

  private static Group addGroup(Scanner s) {
    System.out.println("Informe a Descricao do Grupo");

    String descricao = s.nextLine();
    Group group = new GroupsController(CONNECTION).getGroupByDescription(descricao);

    if (group == null) return new Group(descricao);

    return group;
  }
}
