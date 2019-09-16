package controller;

import model.Contact;
import model.Group;
import model.Phone;
import service.DataService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactController {

  public Contact create(Contact c) {
    String sql = "INSERT INTO contacts(first_name, last_name, email) VALUES(?, ?, ?);";

    try {
      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);

      pstmt.setString(1, c.getFirstName());
      pstmt.setString(2, c.getLastName());
      pstmt.setString(3, c.getEmail());
      pstmt.executeUpdate();
      c.setContactId(pstmt.getGeneratedKeys().getInt(1));

      new PhoneController().create(c.getPhones());
      new GroupsController().create(c.getGroups());
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
      Statement stmt = DataService.CONNECTION.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        contacts.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
      }

      contacts.forEach(c -> {
        c.setPhones(new PhoneController().getPhonesByContactId(c.getContactId()));
        c.setGroups(new GroupsController().getGroupsByContactId(c.getContactId()));
      });

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return contacts;
  }

  public Contact getContactById(Integer id) {
    String sql = "SELECT * FROM contacts WHERE contact_id = ?;";

    try {
      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);

      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      return new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public Contact getContactByName(String nome) {
    String sql = "SELECT * FROM contacts WHERE first_name = ?;";

    try {
      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
      pstmt.setString(1, nome);
      ResultSet rs = pstmt.executeQuery();

      return new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  private void createContactsPhones(Contact c) {
    String sql = "INSERT INTO contacts_phones(contact_id, phone_id) VALUES (?, ?);";

    for (Phone p : c.getPhones()) {
      try {
        PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
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
        PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
        pstmt.setInt(1, c.getContactId());
        pstmt.setInt(2, g.getGroupId());
        pstmt.executeUpdate();

      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }

  }


}
