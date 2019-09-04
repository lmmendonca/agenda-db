import model.Contact;

import java.sql.*;

public class Main {
    private static final String URL = "jdbc:sqlite:db:sqlite";

    public static void main(String[] args) {

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

    public static void executeSql(String sql) {
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

    public static void insert(int id, String firstName, String lastName, String email) {
        String sql = "INSERT INTO contacts VALUES(?, ?, ?, ?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    private static void insertContact(Contact c) {
        String sql = "INSERT INTO contacts(first_name, last_name, email) VALUES(?, ?, ?, ?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(2, c.getFirstName());
            pstmt.setString(3, c.getLastName());
            pstmt.setString(4, c.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertPhone(int id, String phone) {
        String sql = "INSERT INTO phones VALUES(?, ?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertGroup(int id, String description) {
        String sql = "INSERT INTO groups VALUES(?, ?);";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAllContacts() {
        String sql = "SELECT * FROM contacts;";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " +
                        rs.getString(2) + " " + rs.getString(3)
                        + " " + rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getContactById(Integer id) {
        String sql = "SELECT * FROM contacts WHERE contact_id = ?;";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " +
                        rs.getString(2) + " " + rs.getString(3)
                        + " " + rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getContactByName(String nome) {
        String sql = "SELECT * FROM contacts WHERE first_name = ?;";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " +
                        rs.getString(2) + " " + rs.getString(3)
                        + " " + rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
