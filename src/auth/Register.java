/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Scanner;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Register {
    static Connection koneksi;
    static PreparedStatement ps;

    static String role;
    static String nama;
    static String username;
    static String simpanPass;

    static Scanner input = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        menu();
    }

    public static void menu() {
        String add_data = "INSERT INTO user (role, nama, username, password) VALUES(?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");

            System.out.println(" ");
            System.out.println("+--------------------------------+");
            System.out.println("|   HALAMAN REGISTRASI PENGGUNA  |");
            System.out.println("+--------------------------------+");
            System.out.println(" ");

            loginScanner();

            ps = koneksi.prepareStatement(add_data);

            ps.setString(1, role.toLowerCase());
            ps.setString(2, nama);
            ps.setString(3, username);
            ps.setString(4, simpanPass);

            int total_data = ps.executeUpdate();

            if (total_data > 0) {
                System.out.println("Berhasil Register!\n");
                Login.menu();
            } else {
                System.out.println("Gagal Register!\n");
                Menu_auth.menu();
            }

            koneksi.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loginScanner() {
        String password;
        System.out.print("Masukkan role (admin, petugas): ");
        role = input.nextLine().trim();
        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("petugas")) {
            System.out.println("Maaf, role tidak ditemukan!");
            loginScanner();
        } else {
            System.out.print("Masukkan nama: ");
            nama = input.nextLine().trim();
            System.out.print("Masukkan username: ");
            username = input.nextLine().trim();
            System.out.print("Masukkan password: ");
            password = input.nextLine().trim();
            simpanPass = password;
        }

    }
}
