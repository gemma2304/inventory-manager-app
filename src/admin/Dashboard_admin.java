package admin;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.Statement;

import auth.Login;
import auth.Menu_auth;
import crud.Crud_barang;
import crud.Crud_user;

public class Dashboard_admin {
    static Scanner input = new Scanner(System.in);
    static Connection koneksi;
    static Statement stmt;

    public static void main(String[] args) {
        Dashboard_admin dashboard = new Dashboard_admin();
        boolean loginStatus = false;
        dashboard.checkLoginStatus(loginStatus);
    }

    public void checkLoginStatus(boolean userLoggedIn) {
        if (userLoggedIn) {
            System.out.println("\nSelamat datang di halaman admin!");
            menu();
        } else {
            Login.main(null);
        }
    }

    public static void menu() {
        System.out.println(" ");
        System.out.println("+--------------------------------+");
        System.out.println("|          HALAMAN ADMIN         |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. CRUD Barang                 |");
        System.out.println("| 2. CRUD User                   |");
        System.out.println("| 3. Logout                      |");
        System.out.println("+--------------------------------+");
        System.out.println(" ");
        System.out.print("Masukkan pilihanmu (1-3): ");

        try {
            int menu = input.nextInt();
            //switch enhancement
            switch (menu) {
                case 1 -> {
                    System.out.print("\n");
                    menu_barang();
                }
                case 2 -> {
                    System.out.print("\n");
                    menu_user();
                }
                case 3 -> {
                    Menu_auth.menu();
                }
                default -> {
                    System.out.println("Pilihan yang kamu masukkan tidak ada di menu!");
                    menu();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Ooops inputan error!");
        }
    }
    public static void menu_barang() {
        Crud_barang.main(null, true);
    }

    public static void menu_user() {
        Crud_user.main(null, true);
    }
}
