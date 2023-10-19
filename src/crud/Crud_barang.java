package crud;

import admin.Dashboard_admin;
import auth.Login;
import auth.Menu_auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crud_barang extends Dashboard_admin {
    static Connection koneksi;
    static ResultSet rs;
    static PreparedStatement ps;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args, boolean loginStatus) {
        Crud_barang barang = new Crud_barang();
        barang.checkLoginStatus(loginStatus);

        try {
            /*
             Ini untuk mendaftarkan driver, jadi driver yang di tambahkan sebelumnya (jdbc driver) harus didaftarin dulu classnya
             Ini drivernya dipakai buat meghubungkan aplikasi JAVA ke DB MySQL
            */
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app", "root", "");

            while (!koneksi.isClosed()) {
                Menu();
            }

            koneksi.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Crud_barang.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Crud_barang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void Menu() {
        System.out.println(" ");
        System.out.println("+------------------------------------+");
        System.out.println("|         MENU CRUD BARANG           |");
        System.out.println("+------------------------------------+");
        System.out.println("| 1.  Tambah Data Barang             |");
        System.out.println("| 2.  Tampilkan Daftar Barang        |");
        System.out.println("| 3.  Edit Data Barang               |");
        System.out.println("| 4.  Hapus Data Barang              |");
        System.out.println("| 5.  Cari Data Barang               |");
        System.out.println("| 6.  Kembali                        |");
        System.out.println("| 7.  Logout                         |");
        System.out.println("+------------------------------------+");
        System.out.println(" ");
        System.out.print("Masukkan pilihanmu (1-7): ");

        try {
            int pilihan = Integer.parseInt(input.readLine());

            /* switch enhanced statement */
            switch (pilihan) {
                case 1 -> add_barang();
                case 2 -> show_barang();
                case 3 -> update_barang();
                case 4 -> delete_barang();
                case 5 -> search_barang();
                case 6 -> Dashboard_admin.menu();
                case 7 -> Menu_auth.menu();
                default -> {
                    System.out.println("Pilihan yang kamu masukkan tidak tersedia!");
                    Menu();
                }
            }
        } catch (Exception e) {
            System.err.println("Ooops pilihan error...");
            System.exit(0);
        }
    }

    private static void show_barang() {
        String get_data = "SELECT * FROM barang ORDER BY harga_jual DESC";
        int nomor_urut = 1;
        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app","root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println("|                                                            DAFTAR BARANG                                                                               |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            System.out.println("|    No\t   |\tId  \t|  Nama Barang         \t|  Nama Supplier     \t|  Harga Beli  \t|  Harga Jual\t| Stok       \t| Tanggal Masuk\t| Tanggal Keluar |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama_barang = rs.getString("nama_barang");
                String nama_supplier = rs.getString("nama_supplier");
                int harga_beli = rs.getInt("harga_beli");
                int harga_jual = rs.getInt("harga_jual");
                int stok = rs.getInt("stok");
                String tanggal_masuk = rs.getString("tanggal_masuk");
                String tanggal_keluar = rs.getString("tanggal_keluar");

                System.out.printf("| %5d    | %5d    \t|  %-20s\t|  %-20s\t|  %-10s \t|  %-10s \t|  %-10s \t|  %-4s \t|  %-4s  \t |%n",
                        nomor_urut, id, nama_barang, nama_supplier, harga_beli, harga_jual, stok,tanggal_masuk,tanggal_keluar);

                nomor_urut++;
            }
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add_barang() {
        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app","root", "");

            Date tanggalMasuk = new Date();

            String tanggal_masuk = dateFormat.format(tanggalMasuk);

            System.out.println("+--------------------------------+");
            System.out.println("|       Tambah Data Barang       |");
            System.out.println("+--------------------------------+");

            System.out.print("Nama Barang: ");
            String nama_barang = input.readLine().trim();
            System.out.print("Nama Supplier: ");
            String nama_supplier = input.readLine().trim();
            System.out.print("Harga Beli: ");
            int harga_beli = Integer.parseInt(input.readLine().trim());
            System.out.print("Harga Jual: ");
            int harga_jual = Integer.parseInt(input.readLine().trim());
            System.out.print("Stok Barang: ");
            int stok = Integer.parseInt(input.readLine().trim());
            System.out.print("Tanggal Barang Keluar (yyyy-MM-dd): ");
            String tanggalKeluar = input.readLine().trim();

            // Format inputan tanggalKeluar
            Date tanggal_keluar = dateFormat.parse(tanggalKeluar);

            // Menyiapkan query
            String sql = "INSERT INTO barang (nama_barang, nama_supplier, harga_beli, harga_jual, stok, tanggal_keluar, tanggal_masuk) VALUE(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, nama_barang);
            ps.setString(2, nama_supplier);
            ps.setInt(3, harga_beli);
            ps.setInt(4, harga_jual);
            ps.setInt(5, stok);
            ps.setDate(6, new java.sql.Date(tanggal_keluar.getTime()));
            ps.setDate(7, java.sql.Date.valueOf(tanggal_masuk));

            int checkData = ps.executeUpdate();

            if (checkData > 0) {
                System.out.println("\nBerhasil menambahkan data barang!");
            } else {
                System.out.println("\nGagal menambahkan data barang!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void update_barang() {
        String get_data = "SELECT * FROM barang ORDER BY harga_jual DESC";
        int nomor_urut = 1;

        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app","root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println("|                                                            DAFTAR BARANG                                                                               |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            System.out.println("|    No\t   |\tId  \t|  Nama Barang         \t|  Nama Supplier     \t|  Harga Beli  \t|  Harga Jual\t| Stok       \t| Tanggal Masuk\t| Tanggal Keluar |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama_barang = rs.getString("nama_barang");
                String nama_supplier = rs.getString("nama_supplier");
                int harga_beli = rs.getInt("harga_beli");
                int harga_jual = rs.getInt("harga_jual");
                int stok = rs.getInt("stok");
                String tanggal_masuk = rs.getString("tanggal_masuk");
                String tanggal_keluar = rs.getString("tanggal_keluar");

                System.out.printf("| %5d    | %5d    \t|  %-20s\t|  %-20s\t|  %-10s \t|  %-10s \t|  %-10s \t|  %-4s \t|  %-4s  \t |%n",
                        nomor_urut, id, nama_barang, nama_supplier, harga_beli, harga_jual, stok,tanggal_masuk,tanggal_keluar);

                nomor_urut++;
            }
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");

            System.out.println(" ");

            // ambil input dari pengguna
            System.out.print("Masukkan id barang yang datanya ingin diedit: ");
            int id = Integer.parseInt(input.readLine().trim());

            String selectQuery = "SELECT * FROM barang WHERE id = ?";
            PreparedStatement selectStatement = koneksi.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet selectedResult = selectStatement.executeQuery();

            if (selectedResult.next()) {
                // Menyimpan Data Sebelumnya
                String namaBarang = selectedResult.getString("nama_barang");
                String namaSupplier = selectedResult.getString("nama_supplier");
                int hargaBeli = selectedResult.getInt("harga_beli");
                int hargaJual = selectedResult.getInt("harga_jual");
                int stokBarang = selectedResult.getInt("stok");
                String tanggalMasuk = selectedResult.getString("tanggal_masuk");
                String tanggalKeluar = selectedResult.getString("tanggal_keluar");

                // Mempersiapkan data baru untuk di update.
                namaBarang = updateField("nama barang", namaBarang, input);
                namaSupplier = updateField("nama supplier", namaSupplier, input);
                hargaBeli = updateFieldInt("harga beli", hargaBeli, input);
                hargaJual = updateFieldInt("harga jual", hargaJual, input);
                stokBarang = updateFieldInt("stok barang", stokBarang, input);
                tanggalMasuk = updateField("tanggal masuk (yyyy-MM-dd)", tanggalMasuk, input);
                tanggalKeluar = updateField("tanggal keluar (yyyy-MM-dd)", tanggalKeluar, input);

                // format tanggal
                Date tanggal_keluar = dateFormat.parse(tanggalKeluar);
                Date tanggal_masuk = dateFormat.parse(tanggalMasuk);

                // query update
                String sql = "UPDATE barang SET nama_barang=?, nama_supplier=?, harga_beli=?, harga_jual=?, stok=?, tanggal_masuk=?,tanggal_keluar=? WHERE id=?";
                ps = koneksi.prepareStatement(sql);

                ps.setString(1, namaBarang);
                ps.setString(2, namaSupplier);
                ps.setInt(3, hargaBeli);
                ps.setInt(4, hargaJual);
                ps.setInt(5, stokBarang);
                ps.setDate(6, new java.sql.Date(tanggal_masuk.getTime()));
                ps.setDate(7, new java.sql.Date(tanggal_keluar.getTime()));
                ps.setInt(8, id);

                int checkData = ps.executeUpdate();

                if (checkData > 0) {
                    System.out.println("\nBerhasil mengedit data barang!");
                } else {
                    System.out.println("\nGagal mengedit data barang!");
                }
            } else {
                System.out.println("\nMaaf, id barang " + id + " tidak ditemukan.");
                Menu();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void delete_barang() {
        String get_data = "SELECT * FROM barang ORDER BY harga_jual DESC";
        int nomor_urut = 1;
        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app",
                    "root", "");
            ps = koneksi.prepareStatement(get_data);
            rs = ps.executeQuery();

            System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println("|                                                            DAFTAR BARANG                                                                               |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            System.out.println("|    No\t   |\tId  \t|  Nama Barang         \t|  Nama Supplier     \t|  Harga Beli  \t|  Harga Jual\t| Stok       \t| Tanggal Masuk\t| Tanggal Keluar |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama_barang = rs.getString("nama_barang");
                String nama_supplier = rs.getString("nama_supplier");
                int harga_beli = rs.getInt("harga_beli");
                int harga_jual = rs.getInt("harga_jual");
                int stok = rs.getInt("stok");
                String tanggal_masuk = rs.getString("tanggal_masuk");
                String tanggal_keluar = rs.getString("tanggal_keluar");

                System.out.printf("| %5d    | %5d    \t|  %-20s\t|  %-20s\t|  %-10s \t|  %-10s \t|  %-10s \t|  %-4s \t|  %-4s  \t |%n",
                        nomor_urut, id, nama_barang, nama_supplier, harga_beli, harga_jual, stok,tanggal_masuk,tanggal_keluar);

                nomor_urut++;
            }
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");

            System.out.println(" ");

            // ambil input dari pengguna
            System.out.print("Masukkan id barang yang datanya ingin dihapus: ");
            int id = Integer.parseInt(input.readLine());

            String selectQuery = "SELECT * FROM barang WHERE id = ?";
            PreparedStatement selectStatement = koneksi.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet selectedResult = selectStatement.executeQuery();

            if (selectedResult.next()) {
                String sql = "DELETE FROM barang WHERE id=?";
                ps = koneksi.prepareStatement(sql);
                ps.setInt(1, id);

                int checkData = ps.executeUpdate();

                if (checkData > 0) {
                    System.out.println("\nBerhasil menghapus data barang!");
                } else {
                    System.out.println("\nGagal menghapus data barang!");
                }
            } else {
                System.out.println("\nMaaf, id barang tidak ditemukan!");
                Menu();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void search_barang() {
        int nomor_urut = 1;

        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/inventory_app",
                    "root", "");
            String query = "SELECT * FROM barang WHERE id LIKE ? OR nama_barang LIKE ? OR nama_supplier LIKE ? OR harga_beli LIKE ? OR harga_jual LIKE ? OR stok LIKE ? OR tanggal_masuk LIKE ? OR tanggal_keluar LIKE ?";
            ps = koneksi.prepareStatement(query);

            System.out.print("Masukkan keyword data barang: ");
            String cari = input.readLine().trim();

            String kata_kunci = '%' + cari + '%';
            ps.setString(1, kata_kunci);
            ps.setString(2, kata_kunci);
            ps.setString(3, kata_kunci);
            ps.setString(4, kata_kunci);
            ps.setString(5, kata_kunci);
            ps.setString(6, kata_kunci);
            ps.setString(7, kata_kunci);
            ps.setString(8, kata_kunci);

            rs = ps.executeQuery();

            // Menyimpan hasil pencarian ke ARRAY
            List<String> hasilPencarian = new ArrayList<>();

            System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.println("|                                                            HASIL PENCARIAN BARANG                                                                      |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            System.out.println("|    No\t   |\tId  \t|  Nama Barang         \t|  Nama Supplier     \t|  Harga Beli  \t|  Harga Jual\t| Stok       \t| Tanggal Masuk\t| Tanggal Keluar |");
            System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama_barang = rs.getString("nama_barang");
                String nama_supplier = rs.getString("nama_supplier");
                int harga_beli = rs.getInt("harga_beli");
                int harga_jual = rs.getInt("harga_jual");
                int stok = rs.getInt("stok");
                String tanggal_masuk = rs.getString("tanggal_masuk");
                String tanggal_keluar = rs.getString("tanggal_keluar");

                hasilPencarian.add(String.format("| %5d    | %5d    \t|  %-20s\t|  %-20s\t|  %-10s \t|  %-10s \t|  %-10s \t|  %-4s \t|  %-4s  \t |",
                        nomor_urut, id, nama_barang, nama_supplier, harga_beli, harga_jual, stok, tanggal_masuk,tanggal_keluar, "+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+"));
                nomor_urut++;
            }
            if (hasilPencarian.isEmpty()) {
                System.out.println("\nTidak ada barang yang cocok dengan kata kunci '" + cari + "'");
                Menu();
            } else {
                //method reference
                hasilPencarian.forEach(System.out::println);
                System.out.println("+----------+------------+-----------------------+-----------------------+---------------+---------------+---------------+---------------+----------------+");
            }

        } catch (SQLException ex) {
            System.err.println("\nTerjadi kesalahan dalam pencarian barang: " + ex.getMessage());
        } catch (IOException e) {
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

    private static Integer updateFieldInt(String namaField, Integer nilaiAwal, BufferedReader input) {
        try {
            System.out.println(" ");
            System.out.print("Apakah kamu ingin mengedit " + namaField + " (y/n)? ");
            String response = input.readLine();

            if ("y".equalsIgnoreCase(response)) {
                System.out.print("Masukkan " + namaField + " baru: ");
                return Integer.parseInt(input.readLine().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nilaiAwal;
    }
}
