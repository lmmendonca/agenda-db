package controller;

import model.Contact;
import service.DataService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactController {

  private Contact insertContact(Contact c) {
    String sql = "INSERT INTO contacts(first_name, last_name, email) VALUES(?, ?, ?);";

    try {
      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);

      pstmt.setString(1, c.getFirstName());
      pstmt.setString(2, c.getLastName());
      pstmt.setString(3, c.getEmail());
      pstmt.executeUpdate();

      c.setContactId(pstmt.getGeneratedKeys().getInt(1));
      return c;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  private List<Contact> getAllContacts() {
    String sql = "SELECT * FROM contacts;";

    List<Contact> list = new ArrayList<>();
    try {
      Statement stmt = DataService.CONNECTION.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        list.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
      }

      return list;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return list;
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

//  public ContactsGroups insertContactGroups(ContactsGroups cg) {
//    String sql = "INSERT INTO contacts_groups(contact_id, group_id) VALUES(?, ?);";
//
//    try {
//      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
//
//      pstmt.setInt(1, cg.getContact().getContactId());
//      pstmt.setInt(2, cg.getGroup().getGroupId());
//      pstmt.executeUpdate();
//
//      cg.setId(pstmt.getGeneratedKeys().getInt(1));
//      return cg;
//
//    } catch (SQLException e) {
//      System.out.println(e.getMessage());
//    }
//
//    return null;
//  }
//
//  public ContactsPhones insertContactPhones(ContactsPhones cp) {
//    String sql = "INSERT INTO contacts_phones(contact_id, phone_id) VALUES(?, ?);";
//
//    try {
//      PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
//
//      pstmt.setInt(1, cp.getContact().getContactId());
//      pstmt.setInt(2, cp.getPhone().getPhoneId());
//      pstmt.executeUpdate();
//
//      cp.setId(pstmt.getGeneratedKeys().getInt(1));
//      return cp;
//
//    } catch (SQLException e) {
//      System.out.println(e.getMessage());
//    }
//
//    return null;
//  }
}
