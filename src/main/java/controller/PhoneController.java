package controller;

import model.Phone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneController {

  private Connection connection;

  public PhoneController(Connection connection) {
    this.connection = connection;
  }

  public Phone create(Phone p) {
    String sql = "INSERT INTO phones(phone) VALUES(?);";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setString(1, p.getPhone());
      pstmt.executeUpdate();

      p.setPhoneId(pstmt.getGeneratedKeys().getInt(1));

      return p;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public List<Phone> create(List<Phone> p) {
    if (p.size() > 0) {

      p.forEach(phone -> {
          if (phone.getPhoneId() == null) create(phone);
      });

      return p;
    }
    return null;
  }

  public Phone getPhoneById(Integer id) {
    String sql = "SELECT * FROM phones WHERE phone_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      return new Phone(rs.getInt(1), rs.getString(2));

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public List<Phone> getPhonesByContactId(Integer id) {
    String sql =
        "SELECT p.phone_id, phone FROM phones p, contacts_phones cp "
            + "where p.phone_id = cp.phone_id "
            + "and cp.contact_id = ?;";

    List<Phone> phones = new ArrayList<>();

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        phones.add(new Phone(rs.getInt(1), rs.getString(2)));
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return phones;
  }

  public Phone getPhoneByPhone(String phone) {
    String sql = "SELECT * FROM phones WHERE phone = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, phone);
      ResultSet rs = pstmt.executeQuery();

      return new Phone(rs.getInt(1), rs.getString(2));

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public Phone delete(Phone p) {
    String sql = "DELETE FROM phones where phone_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);

      pstmt.setInt(1, p.getPhoneId());
      pstmt.executeUpdate();

      return p;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public Phone update(Phone p) {
    String sql = "UPDATE phones SET phone = ? WHERE phone_id = ?;";

    try {
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, p.getPhone());
      pstmt.setInt(2, p.getPhoneId());
      pstmt.executeUpdate();

      return p;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }
}
