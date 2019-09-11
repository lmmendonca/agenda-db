package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataService {
  private static final String URL = "jdbc:sqlite:db:sqlite";
  public static final Connection CONNECTION = connect();

  private static Connection connect() {
    Connection conn = null;

    try {
      conn = DriverManager.getConnection(URL);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
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

    private static void executeSql(String sql) {
        try {
            Statement stmt = CONNECTION.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
