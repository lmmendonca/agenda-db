package controller;

import model.Group;
import service.DataService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GroupsController {

  public GroupsController() {}

    public Group create(Group g) {
        String sql = "INSERT INTO groups(description) VALUES(?);";

        try {
            PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, g.getDescription());
            pstmt.executeUpdate();

            g.setGroupId(
                    pstmt.getGeneratedKeys().getInt(1)
            );

            return g;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Group> create(List<Group> g) {
      if (g.size() > 0) {
          g.forEach(this::create);
          return g;
      }
      return null;
    }

    public Group getGroupByDescription(String description) {
        String sql = "SELECT * FROM groups WHERE description = ?;";

        try {
            PreparedStatement pstmt = DataService.CONNECTION.prepareStatement(sql);
            pstmt.setString(1, description);
            ResultSet rs = pstmt.executeQuery();

            return new Group(
                    rs.getInt(1),
                    rs.getString(2)
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
