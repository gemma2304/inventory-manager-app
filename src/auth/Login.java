package auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import admin.Dashboard_admin;
import petugas.Dashboard_petugas;

public class Login {
    static Connection koneksi;
    static PreparedStatement ps;
    static ResultSet rs;

    static String username;
    static String password;

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static String get_role(String username) throws SQLException {
        String get_role = "SELECT role FROM user WHERE username = ?";

        ps = koneksi.prepareStatement(get_role);
        ps.setString(1, username);

        rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("role");
        } else {
            return null;
        }
    }

    public static void menu() {
        Dashboard_admin admin = new Dashboard_admin();
        Dashboard_petugas petugas = new Dashboard_petugas();

        String login_query = "SELECT username, role FROM user WHERE username=? AND password=?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");

            System.out.println(" ");
            System.out.println("+--------------------------------+");
            System.out.println("|         HALAMAN LOGIN          |");
            System.out.println("+--------------------------------+");
            System.out.print("Masukkan username: ");
            username = input.nextLine().trim();
            System.out.print("Masukkan password: ");
            password = input.nextLine().trim();

            ps = koneksi.prepareStatement(login_query);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next()) {
                boolean loginStatus = true;
                if (get_role(username).equals("admin")) {
                    admin.checkLoginStatus(loginStatus);
                } else if (get_role(username).equals("petugas")) {
                    petugas.checkLoginStatus(loginStatus);
                } else {
                    System.out.println("ERROR");
                }
            } else {
                System.out.println("GAGAL LOGIN!");
            }

            rs.close();
            ps.close();
            koneksi.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
