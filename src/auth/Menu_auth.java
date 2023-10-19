package auth;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu_auth {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        System.out.println(" ");
        System.out.println("+--------------------------------+");
        System.out.println("|        HALAMAN MENU AUTH       |");
        System.out.println("+--------------------------------+");
        System.out.println("| 1. Login                       |");
        System.out.println("| 2. Register                    |");
        System.out.println("| 3. Keluar                      |");
        System.out.println("+--------------------------------+");
        System.out.println(" ");
        System.out.print("Masukkan pilihanmu (1-3): ");

        try {
            int menu = input.nextInt();
            switch (menu) {
                case 1 -> {
                    System.out.print(" ");
                    Login.main(null);
                }
                case 2 -> {
                    System.out.print(" ");
                    Register.main(null);
                }
                case 3 -> {
                    System.exit(0);
                }
                default -> System.out.println("Pilihan yang kamu masukkan tidak ada di menu!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Ooops inputan error!");
        }
    }
}
