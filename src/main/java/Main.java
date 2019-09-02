import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String URL = "jdbc:sqlite:db.sqlite";


    private static Connection connect() {
        // SQLite connection string
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
            conn = DriverManager.getConnection("jdbc:sqlite:db:sqlite");
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


    public static void main(String[] args) {

        String tGroups = "create table if not exists main.groups\n" +
                "(\n" +
                "\tgroup_id int not null\n" +
                "\t\tconstraint groups_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tdescription text not null\n" +
                ");";

        String tContacts = "create table if not exists main.contacts\n" +
                "(\n" +
                "\tcontact_id int not null\n" +
                "\t\tconstraint contacts_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tfirst_name text not null,\n" +
                "\tlast_name text not null,\n" +
                "\temail text not null\n" +
                ");\n" +
                "\n" +
                "create unique index contacts_email_uindex\n" +
                "\ton contacts (email);";

        String tPhones = "create table if not exists main.phones\n" +
                "(\n" +
                "\tphone_id int not null\n" +
                "\t\tconstraint phones_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tphone text not null\n" +
                ");\n";

        String tContactsGroups = "create table if not exists main.contacts_groups\n" +
                "(\n" +
                "\tid int not null\n" +
                "\t\tconstraint contacts_groups_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tcontact_id int not null\n" +
                "\t\tconstraint contact_id__fk\n" +
                "\t\t\treferences contacts,\n" +
                "\tgroup_id int not null\n" +
                "\t\tconstraint group_id__fk\n" +
                "\t\t\treferences groups\n" +
                ");";

        String tContactsPhones = "create table if not exists main.contacts_phones\n" +
                "(\n" +
                "\tid int not null\n" +
                "\t\tconstraint contacts_phones_pk\n" +
                "\t\t\tprimary key,\n" +
                "\tcontact_id int not null\n" +
                "\t\tconstraint p_contacts_id__fk\n" +
                "\t\t\treferences contacts,\n" +
                "\tphone_id int not null\n" +
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
}
