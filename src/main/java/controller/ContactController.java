package controller;

import model.Contact;
import model.Group;
import model.Phone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactController {

  private Connection connection;

  public ContactController(Connection connection) {
    this.connection = connection;
  }

  public Contact create(Contact c) {
    String sql = "INSERT INTO contacts(first_name, last_name, email) VALUES(?, ?, ?);";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setString(1, c.getFirstName());
      pstmt.setString(2, c.getLastName());
      pstmt.setString(3, c.getEmail());
      pstmt.executeUpdate();
      c.setContactId(pstmt.getGeneratedKeys().getInt(1));

      new PhoneController(connection).create(c.getPhones());
      new GroupsController(connection).create(c.getGroups());
      createContactsPhones(c);
      createContactsGroups(c);

      return c;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public List<Contact> getAll() {
    String sql = "SELECT * FROM contacts;";

    List<Contact> contacts = new ArrayList<>();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        contacts.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
      }

      contacts.forEach(
          c -> {
            c.setPhones(new PhoneController(connection).getPhonesByContactId(c.getContactId()));
            c.setGroups(new GroupsController(connection).getGroupsByContactId(c.getContactId()));
          });

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return contacts;
  }

  public Contact getContactById(Integer id) {
    String sql = "SELECT * FROM contacts WHERE contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      return new Contact(
          id,
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          new GroupsController(connection).getGroupsByContactId(id),
          new PhoneController(connection).getPhonesByContactId(id));

    } catch (SQLException ignored) {
    }

    return null;
  }

  public Contact getContactByName(String nome) {
    String sql = "SELECT * FROM contacts WHERE (first_name || ' ' || last_name) = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, nome);
      ResultSet rs = pstmt.executeQuery();

      Integer id = rs.getInt(1);

      return new Contact(
          id,
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          new GroupsController(connection).getGroupsByContactId(id),
          new PhoneController(connection).getPhonesByContactId(id));

    } catch (SQLException ignored) {
    }

    return null;
  }

  public void createContactsPhones(Contact c) {
    String sql = "INSERT INTO contacts_phones(contact_id, phone_id) VALUES (?, ?);";

    for (Phone p : c.getPhones()) {
      try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, c.getContactId());
        pstmt.setInt(2, p.getPhoneId());
        pstmt.executeUpdate();

      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

    public void createContactsPhones(Contact c, List<Phone> phones) {
        String sql = "INSERT INTO contacts_phones(contact_id, phone_id) VALUES (?, ?);";

        for (Phone p : phones) {
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, c.getContactId());
                pstmt.setInt(2, p.getPhoneId());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createContactsGroups(Contact c) {
    String sql = "INSERT INTO contacts_groups(contact_id, group_id) VALUES (?, ?);";

    for (Group g : c.getGroups()) {
      try {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, c.getContactId());
        pstmt.setInt(2, g.getGroupId());
        pstmt.executeUpdate();

      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

    public void createContactsGroups(Contact c, List<Group> groups) {
        String sql = "INSERT INTO contacts_groups(contact_id, group_id) VALUES (?, ?);";

        for (Group g : groups) {
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, c.getContactId());
                pstmt.setInt(2, g.getGroupId());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

  public Contact delete(Contact c) {
    String sql = "DELETE FROM contacts where contact_id = ?;";

    validateDeleteGroupNecessary(c);
    validateDeletePhoneNecessary(c);

    deleteContactsGroupsByContactId(c);
    deleteContactsPhonesByContactId(c);

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, c.getContactId());
      pstmt.executeUpdate();

      return c;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  private void deleteContactsGroupsByContactId(Contact c) {
    String sql = "DELETE FROM contacts_groups WHERE contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, c.getContactId());
      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public List<Contact> getContactByPhoneId(Integer id) {
    String sql =
        "SELECT c.contact_id, c.first_name, c.last_name, c.email FROM contacts c, contacts_phones cp "
            + "where c.contact_id = cp.contact_id "
            + "and cp.phone_id = ?;";

    return getContacts(id, sql);
  }

  public List<Contact> getContactByGroupId(Integer id) {
    String sql =
        "SELECT c.contact_id, c.first_name, c.last_name, c.email FROM contacts c, contacts_groups cg "
            + "where c.contact_id = cg.contact_id "
            + "and cg.group_id = ?;";

    return getContacts(id, sql);
  }

  private List<Contact> getContacts(Integer id, String sql) {
    List<Contact> contacts = new ArrayList<>();

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        contacts.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return contacts;
  }

  private void deleteContactsPhonesByContactId(Contact c) {
    String sql = "DELETE FROM contacts_phones WHERE contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, c.getContactId());
      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void validateDeletePhoneNecessary(Contact c) {
    c.getPhones()
        .forEach(
            p -> {
              int size = getContactByPhoneId(p.getPhoneId()).size();
              if (!(size > 1)) new PhoneController(connection).delete(p);
            });
  }

  private void validateDeleteGroupNecessary(Contact c) {
    c.getGroups()
        .forEach(
            g -> {
              int size = getContactByGroupId(g.getGroupId()).size();
              if (!(size > 1)) new GroupsController(connection).delete(g);
            });
  }

  public Contact update(Contact c) {
    String sql =
        "UPDATE contacts "
            + "SET first_name = ?, "
            + "last_name = ?, "
            + "email = ? "
            + "WHERE contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, c.getFirstName());
      pstmt.setString(2, c.getLastName());
      pstmt.setString(3, c.getEmail());
      pstmt.setInt(4, c.getContactId());
      pstmt.executeUpdate();

      return c;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public void deleteContactsGroups(Contact c, Group g) {
    String sql = "DELETE FROM contacts_groups WHERE group_id = ? and contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, g.getGroupId());
      pstmt.setInt(2, c.getContactId());
      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteContactsPhones(Contact c, Phone p) {
    String sql = "DELETE FROM contacts_phones WHERE phone_id = ? and contact_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, p.getPhoneId());
      pstmt.setInt(2, c.getContactId());
      pstmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void removePhone(Contact c, Phone p) {
    validateDeletePhoneNecessary(c);
    deleteContactsPhones(c, p);
  }

  public void removeGroup(Contact c, Group g) {
    validateDeletePhoneNecessary(c);
    deleteContactsGroups(c, g);
  }
}
