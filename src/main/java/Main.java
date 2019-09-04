import model.Contact;
import model.Group;
import model.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String URL = "jdbc:sqlite:db:sqlite";

    public static void main(String[] args) {
//        insertContact(new Contact("Elias", "Red", "elias@gmail.com"));
//        insertPhone(new Phone("47991411323"));
//        insertGroup(new Group("Favoritos"));

        getAllContacts().forEach(System.out::println);

    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void executeSql(String sql) {
        Connection conn = null;
        try {
            conn = connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTeble() {
        String tGroups = "create table if not exists main.groups\n" +
                "(\n" +
                "\tgroup_id integer not null\n" +
                "\t\tconstraint groups_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tdescription text not null\n" +
                ");";

        String tContacts = "create table if not exists main.contacts\n" +
                "(\n" +
                "\tcontact_id integer not null\n" +
                "\t\tconstraint contacts_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tfirst_name text not null,\n" +
                "\tlast_name text not null,\n" +
                "\temail text not null\n" +
                ");\n" +
                "\n" +
                "create unique index contacts_email_uindex\n" +
                "\ton contacts (email);";

        String tPhones = "create table if not exists main.phones\n" +
                "(\n" +
                "\tphone_id integer not null\n" +
                "\t\tconstraint phones_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tphone text not null\n" +
                ");\n";

        String tContactsGroups = "create table if not exists main.contacts_groups\n" +
                "(\n" +
                "\tid integer not null\n" +
                "\t\tconstraint contacts_groups_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tcontact_id integer not null\n" +
                "\t\tconstraint contact_id__fk\n" +
                "\t\t\treferences contacts,\n" +
                "\tgroup_id integer not null\n" +
                "\t\tconstraint group_id__fk\n" +
                "\t\t\treferences groups\n" +
                ");";

        String tContactsPhones = "create table if not exists main.contacts_phones\n" +
                "(\n" +
                "\tid integer not null\n" +
                "\t\tconstraint contacts_phones_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tcontact_id integer not null\n" +
                "\t\tconstraint p_contacts_id__fk\n" +
                "\t\t\treferences contacts,\n" +
                "\tphone_id integer not null\n" +
                "\t\tconstraint c_phones___fk\n" +
                "\t\t\treferences phones\n" +
                ");";

        // criacao das tabelas
        executeSql(tGroups);
        executeSql(tContacts);
        executeSql(tPhones);
        executeSql(tContactsGroups);
        executeSql(tContactsPhones);
    }

    private static Contact insertContact(Contact c) {
        String sql = "INSERT INTO contacts(first_name, last_name, email) VALUES(?, ?, ?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, c.getFirstName());
            pstmt.setString(2, c.getLastName());
            pstmt.setString(3, c.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return c;
    }

    private static Phone insertPhone(Phone p) {
        String sql = "INSERT INTO phones(phone) VALUES(?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, p.getPhone());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return p;
    }

    private static Group insertGroup(Group g) {
        String sql = "INSERT INTO groups(description) VALUES(?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, g.getDescription());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return g;

    }

    private static List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts;";

        List<Contact> list = new ArrayList<>();
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Contact(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }

            return list;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public static Contact getContactById(Integer id) {
        String sql = "SELECT * FROM contacts WHERE contact_id = ?;";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            return new Contact(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Contact getContactByName(String nome) {
        String sql = "SELECT * FROM contacts WHERE first_name = ?;";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            return new Contact(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


}
