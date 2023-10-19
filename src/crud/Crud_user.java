package crud;

import admin.Dashboard_admin;
import auth.Login;
import auth.Menu_auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crud_user extends Dashboard_admin {
    static Connection koneksi;
    static ResultSet rs;
    static PreparedStatement ps;

    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    static Scanner pertanyaan = new Scanner(System.in);

    static String role;
    static String nama;
    static String username;
    static String password;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args, boolean loginStatus) {
        Crud_user user = new Crud_user();
        user.checkLoginStatus(loginStatus);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Menu();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Crud_user.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void Menu() {

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println(" ");
            System.out.println("+------------------------------------+");
            System.out.println("|        MENU CRUD PENGGUNA          |");
            System.out.println("+------------------------------------+");
            System.out.println("| 1.  Tambah Data Pengguna           |");
            System.out.println("| 2.  Tampilkan Daftar Pengguna      |");
            System.out.println("| 3.  Edit Data Pengguna             |");
            System.out.println("| 4.  Hapus Data Pengguna            |");
            System.out.println("| 5.  Kembali                        |");
            System.out.println("| 6.  Logout                         |");
            System.out.println("+------------------------------------+");
            System.out.println(" ");
            System.out.print("Masukkan pilihanmu (1-6): ");

            try {
                int pilihan = Integer.parseInt(input.readLine());

                switch (pilihan) {
                    case 1 -> add_user();
                    case 2 -> show_user();
                    case 3 -> update_user();
                    case 4 -> delete_user();
                    case 5 -> Dashboard_admin.menu();
                    case 6 -> Menu_auth.menu();
                    default -> System.out.println("Pilihan yang kamu masukkan tidak tersedia!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(" ");
            System.out.print("Ingin melanjutkan? (y/n) : ");
            String response = pertanyaan.nextLine();

            if ("n".equalsIgnoreCase(response)) {
                continueRunning = false;
                System.exit(0);
            } else if ("y".equalsIgnoreCase(response)) {
                Menu();
            } else {
                inputUlang(response);
            }
        }
    }

    static void show_user() {
        String get_data = "SELECT * FROM user";
        int nomor_urut = 1;

        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+---------------------------------------------------------------------------------------------------+");
            System.out.println("|                                  DAFTAR PENGGUNA                                                  |");
            System.out.println("+-------+-----------------------------------------+-------------+---------------+-------------------+");
            System.out.println("| No \t| Id \t  |  Nama             \t\t\t  |  Username   \t|  Password    \t|  Role \t\t|");
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");

            while (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                String nama = rs.getString("nama");
                String username = rs.getString("username");
                String password = rs.getString("password");

                System.out.printf("| %-5d | %-5d   |  %-20s\t\t  |  %-10s \t| %-10s \t|  %-10s \t|  %n",
                        nomor_urut, id, nama, username, password, role);
                nomor_urut++;
            }
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void add_user() {
        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");

            System.out.println("+--------------------------------+");
            System.out.println("|       Tambah Data Pengguna     |");
            System.out.println("+--------------------------------+");

            inputData();

            // Menyiapkan query
            String add_user = "INSERT INTO user (role, nama, username, password) VALUES(?, ?, ?, ?)";
            ps = koneksi.prepareStatement(add_user);
            ps.setString(1, role.toLowerCase());
            ps.setString(2, nama);
            ps.setString(3, username);
            ps.setString(4, password);

            // Tambah ke database
            int checkData = ps.executeUpdate();

            if (checkData > 0) {
                System.out.println("\nBerhasil menambahkan data pengguna!");
                koneksi.close();
            } else {
                System.out.println("\nGagal menambahkan data pengguna!");
                koneksi.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update_user() {
        String get_data = "SELECT * FROM user";
        int nomor_urut = 1;

        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+---------------------------------------------------------------------------------------------------+");
            System.out.println("|                                  DAFTAR PENGGUNA                                                  |");
            System.out.println("+-------+-----------------------------------------+-------------+---------------+-------------------+");
            System.out.println("| No \t| Id \t  |  Nama             \t\t\t  |  Username   \t|  Password    \t|  Role \t\t|");
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");

            while (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                String nama = rs.getString("nama");
                String username = rs.getString("username");
                String password = rs.getString("password");

                System.out.printf("| %-5d | %-5d   |  %-20s\t\t  |  %-10s \t| %-10s \t|  %-10s \t|  %n",
                        nomor_urut, id, nama, username, password, role);
                nomor_urut++;
            }
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");

            System.out.println(" ");
            System.out.print("Masukkan id pengguna yang datanya ingin diedit: ");
            int id = Integer.parseInt(input.readLine());

            String selectQuery = "SELECT * FROM user WHERE id = ?";
            PreparedStatement selectStatement = koneksi.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet selectedResult = selectStatement.executeQuery();

            if (selectedResult.next()) {
                String userRole = selectedResult.getString("role");
                String namaUser = selectedResult.getString("nama");
                String userName = selectedResult.getString("username");
                String userPass = selectedResult.getString("password");

                userRole = updateRole("role", userRole, input);
                namaUser = updateField("nama", namaUser, input);
                userName = updateField("username", userName, input);
                userPass = updateField("password", userPass, input);

                // query update
                String sql = "UPDATE user SET role=?, nama=?, username=?, password=? WHERE id=?";
                ps = koneksi.prepareStatement(sql);
                ps.setString(1, userRole.toLowerCase());
                ps.setString(2, namaUser);
                ps.setString(3, userName);
                ps.setString(4, userPass);
                ps.setInt(5, id);

                int checkData = ps.executeUpdate();
                if (checkData > 0) {
                    System.out.println("\nBerhasil mengedit data pengguna!");
                } else {
                    System.out.println("\nGagal mengedit data pengguna");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void delete_user() {
        String get_data = "SELECT * FROM user";
        int nomor_urut = 1;

        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+---------------------------------------------------------------------------------------------------+");
            System.out.println("|                                  DAFTAR PENGGUNA                                                  |");
            System.out.println("+-------+-----------------------------------------+-------------+---------------+-------------------+");
            System.out.println("| No \t| Id \t  |  Nama             \t\t\t  |  Username   \t|  Password    \t|  Role \t\t|");
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");

            while (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                String nama = rs.getString("nama");
                String username = rs.getString("username");
                String password = rs.getString("password");

                System.out.printf("| %-5d | %-5d   |  %-20s\t\t  |  %-10s \t| %-10s \t|  %-10s \t|  %n",
                        nomor_urut, id, nama, username, password, role);
                nomor_urut++;
            }
            System.out.println("+-------+---------+-------------------------------+-----------------+---------------+---------------+");

            System.out.println(" ");
            System.out.print("Masukkan id pengguna yang datanya ingin dihapus: ");
            int id = Integer.parseInt(input.readLine());

            String selectQuery = "SELECT * FROM user WHERE id = ?";

            PreparedStatement selectStatement = koneksi.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet selectedResult = selectStatement.executeQuery();

            if (selectedResult.next()) {
                String sql = "DELETE FROM user WHERE id=?";
                ps = koneksi.prepareStatement(sql);
                ps.setInt(1, id);

                int checkData = ps.executeUpdate();

                if (checkData > 0) {
                    System.out.println("\nBerhasil menghapus data pengguna!");
                } else {
                    System.out.println("\nGagal menghapus data pengguna!");
                }
            } else {
                System.out.println("\nMaaf, id pengguna tidak ditemukan!");
                Menu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method overriding
    @Override
    public void checkLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            Menu();
        } else {
            Login.menu();
        }
    }

    // Function tambahan.
    public static void inputUlang(String salahInput) {
        boolean validResponse = false;

        System.out.println("Kamu tidak bisa memasukkan " + salahInput);
        System.out.print("Ingin melanjutkan? (y/n) : ");
        String response = pertanyaan.nextLine();

        while (!validResponse) {
            if ("n".equalsIgnoreCase(response)) {
                validResponse = true;
                System.exit(0);
            } else if ("y".equalsIgnoreCase(response)) {
                Menu();
            } else {
                System.out.println("Kamu tidak bisa memasukkan " + response);
                System.out.print("Ingin melanjutkan? (y/n) : ");
                response = pertanyaan.nextLine();
            }
        }
    }

    public static void inputData() {
        try {
            System.out.print("Masukkan role baru (admin, petugas): ");
            role = input.readLine();
            if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("petugas")) {
                System.out.println("Ooops role yang kamu isi tidak ada... \n");
                inputData();
            } else {
                System.out.print("Masukkan nama: ");
                nama = input.readLine();
                System.out.print("Masukkan username: ");
                username = input.readLine();
                System.out.print("Masukkan password: ");
                password = input.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String updateRole(String namaField, String nilaiAwal, BufferedReader input) {
        try {
            boolean inputValid = false;
            String newRole = "";

            while (!inputValid) {
                System.out.println(" ");
                System.out.print("Apakah kamu ingin mengedit " + namaField + " (y/n)? ");
                String response = input.readLine();

                if ("y".equalsIgnoreCase(response)) {
                    System.out.print("Masukkan " + namaField + " (admin/petugas) baru: ");
                    newRole = input.readLine();
                    if (newRole.toLowerCase().equals("admin") || newRole.toLowerCase().equals("petugas")) {
                        inputValid = true; // Input valid, keluar dari loop
                    } else {
                        System.out.println("Role yang diinput harus 'admin' atau 'petugas'. Silakan input kembali.");
                    }
                } else {
                    newRole = nilaiAwal;
                    inputValid = true; // Tidak ingin mengupdate, keluar dari loop
                }
            }

            return newRole.toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nilaiAwal.toLowerCase();
    }

    private static String updateField(String namaField, String nilaiAwal, BufferedReader input) {
        try {
            System.out.println(" ");
            System.out.print("Apakah kamu ingin mengedit " + namaField + " (y/n)? ");
            String response = input.readLine();

            if ("y".equalsIgnoreCase(response)) {
                System.out.print("Masukkan " + namaField + " baru: ");
                return input.readLine().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nilaiAwal;
    }

}
