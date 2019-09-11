package controller;

import model.Phone;
import service.DataService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneController {

    public PhoneController() {
    }

    private Phone insertPhone(Phone p) {
        String sql = "INSERT INTO phones(phone) VALUES(?);";

        try {
            PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);

            pstmt.setString(1, p.getPhone());
            pstmt.executeUpdate();

            p.setPhoneId(
                    pstmt.getGeneratedKeys().getInt(1)
            );

            return p;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Phone getPhoneById(Integer id) {
        String sql = "SELECT * FROM phones WHERE phone_id = ?;";

        try {
            PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            return new Phone(
                    rs.getInt(1),
                    rs.getString(2)
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Phone getPhoneByPhone(String phone) {
        String sql = "SELECT * FROM phones WHERE phone = ?;";

        try {
            PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();

            return new Phone(
                    rs.getInt(1),
                    rs.getString(2)
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
