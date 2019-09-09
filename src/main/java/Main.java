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
        String tGroups =
                "create table if not exists main.groups (" +
                        "group_id integer not null constraint groups_pk primary key autoincrement, " +
                        "description text not null);";

        String tContacts =
                "create table if not exists main.contacts (" +
                        "contact_id integer not null constraint contacts_pk primary key autoincrement," +
                        "first_name text not null," +
                        "last_name text not null," +
                        "email text not null );" +
                        "create unique index contacts_email_uindex on contacts (email);";

        String tPhones =
                "create table if not exists main.phones ("
                        + "phone_id integer not null constraint phones_pk primary key autoincrement,"
                        + "phone text not null);";

        String tContactsGroups =
                "create table if not exists main.contacts_groups(" +
                        "id integer not null constraint contacts_groups_pk primary key autoincrement,"
                        + "contact_id integer not null constraint contact_id__fk references contacts,"
                        + "group_id integer not null constraint group_id__fk references groups);";

        String tContactsPhones =
                "create table if not exists main.contacts_phones (" +
                        "id integer not null constraint contacts_phones_pk primary key autoincrement, " +
                        "contact_id integer not null constraint p_contacts_id__fk references contacts," +
                        "phone_id integer not null constraint c_phones___fk references phones);";

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

            c.setContactId(pstmt.getGeneratedKeys().getInt(1));
            return c;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static Phone insertPhone(Phone p) {
        String sql = "INSERT INTO phones(phone) VALUES(?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, p.getPhone());
            pstmt.executeUpdate();

            p.setPhoneId(pstmt.getGeneratedKeys().getInt(1));

            return p;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static Group insertGroup(Group g) {
        String sql = "INSERT INTO groups(description) VALUES(?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, g.getDescription());
            pstmt.executeUpdate();

            g.setGroupId(pstmt.getGeneratedKeys().getInt(1));
            return g;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts;";

        List<Contact> list = new ArrayList<>();
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
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

    public static Contact getContactById(Integer id) {
        String sql = "SELECT * FROM contacts WHERE contact_id = ?;";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            return new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));

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
            return new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
