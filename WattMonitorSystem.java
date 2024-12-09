import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class WattMonitorSystem {

    private static Map<String, Pengguna> pengguna = new HashMap<>();
    private static Map<String, Perangkat> perangkat = new HashMap<>();
    private static final String FILE_PENGGUNA = "pengguna.txt";
    private static final String FILE_PERANGKAT = "perangkat.txt";
    private static final double BIAYA_PER_KWH = 0.15; // Biaya per kWh dalam dolar
    private static final int BATAS_DAYA = 1500; // Ambang batas daya (dalam Watt)

    public static void main(String[] args) {
        muatPengguna();
        if (!muatPerangkat()) {
            inisialisasiPerangkatDefault();
            simpanPerangkat();
        }
    
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();
            
            switch (pilihan) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    simpanPengguna();
                    simpanPerangkat();
                    System.out.println("Selamat tinggal!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }
    }
    
    private static void login(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        if ("admin".equals(username) && "admin123".equals(password)) {
            menuAdmin(scanner);
        } else if (pengguna.containsKey(username) && pengguna.get(username).getPassword().equals(password)) {
            menuPengguna(scanner, pengguna.get(username));
        } else {
            System.out.println("Username atau password salah.");
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();
        System.out.print("Masukkan nama: ");
        String nama = scanner.nextLine();

        if (!pengguna.containsKey(username)) {
            Pengguna penggunaBaru = new Pengguna(username, password, nama);
            pengguna.put(username, penggunaBaru);
            simpanPengguna();
            System.out.println("Pendaftaran berhasil!");
        } else {
            System.out.println("Username sudah ada.");
        }
    }

    private static void menuAdmin(Scanner scanner) {
        while (true) {
            System.out.println("Menu Admin:");
            System.out.println("1. Tambah Perangkat");
            System.out.println("2. Edit Perangkat");
            System.out.println("3. Lihat Semua Perangkat");
            System.out.println("4. Logout");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();
            switch (pilihan) {
                case 1 -> tambahPerangkat(scanner);
                case 2 -> editPerangkat(scanner);
                case 3 -> lihatPerangkat();
                case 4 -> {
                    System.out.println("Logout.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void tambahPerangkat(Scanner scanner) {
        System.out.print("Masukkan ID perangkat: ");
        String id = scanner.nextLine();
        System.out.print("Masukkan nama perangkat: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan daya perangkat (Watt): ");
        int daya = scanner.nextInt();
        scanner.nextLine();

        Perangkat perangkatBaru = new Perangkat(id, nama, daya);
        perangkat.put(id, perangkatBaru);
        simpanPerangkat();
        System.out.println("Perangkat berhasil ditambahkan!");
    }

    private static void editPerangkat(Scanner scanner) {
        System.out.print("Masukkan ID perangkat yang ingin diedit: ");
        String id = scanner.nextLine();
        Perangkat perangkatEdit = perangkat.get(id);

        if (perangkatEdit != null) {
            System.out.print("Masukkan nama baru (kosongkan untuk tetap sama): ");
            String nama = scanner.nextLine();
            if (!nama.isEmpty()) perangkatEdit.setNama(nama);

            System.out.print("Masukkan daya baru (kosongkan untuk tetap sama): ");
            String daya = scanner.nextLine();
            if (!daya.isEmpty()) {
                try {
                    int dayaInt = Integer.parseInt(daya);
                    perangkatEdit.setDaya(dayaInt);
                } catch (NumberFormatException e) {
                    System.out.println("Input daya tidak valid, harus berupa angka.");
                }
            }

            System.out.println("Perangkat berhasil diperbarui!");
            simpanPerangkat();
        } else {
            System.out.println("Perangkat tidak ditemukan.");
        }
    }

    private static void lihatPerangkat() {
        if (perangkat.isEmpty()) {
            System.out.println("Tidak ada perangkat.");
        } else {
            System.out.println("Perangkat yang tersedia:");
            for (Perangkat p : perangkat.values()) {
                System.out.println(p);
                p.cekPeringatanDaya();  // Mengecek peringatan daya perangkat
            }
        }
    }

    private static void menuPengguna(Scanner scanner, Pengguna pengguna) {
        while (true) {
            System.out.println("Menu Pengguna:");
            System.out.println("1. Lihat Perangkat");
            System.out.println("2. Lihat Notifikasi");
            System.out.println("3. Lihat Laporan Penggunaan Energi");
            System.out.println("4. Hapus Notifikasi");
            System.out.println("5. Logout");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();
            switch (pilihan) {
                case 1 -> lihatPerangkat();
                case 2 -> lihatNotifikasi(pengguna);
                case 3 -> lihatLaporanPenggunaanEnergi(pengguna);
                case 4 -> hapusNotifikasi(pengguna);
                case 5 -> {
                    System.out.println("Logout.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void lihatNotifikasi(Pengguna pengguna) {
        if (pengguna.getNotifikasi().isEmpty()) {
            System.out.println("Tidak ada notifikasi.");
        } else {
            System.out.println("Notifikasi Anda:");
            for (String notifikasi : pengguna.getNotifikasi()) {
                System.out.println(notifikasi);
            }
        }
    }

    private static void hapusNotifikasi(Pengguna pengguna) {
        pengguna.getNotifikasi().clear();  // Menghapus semua notifikasi
        System.out.println("Semua notifikasi telah dihapus.");
    }

    private static void lihatLaporanPenggunaanEnergi(Pengguna pengguna) {
        System.out.println("Laporan Penggunaan Energi:");
        double totalPenggunaan = 0;
        for (Perangkat p : perangkat.values()) {
            totalPenggunaan += p.getDaya();
        }
        double totalBiaya = totalPenggunaan * BIAYA_PER_KWH;
        System.out.println("Total Penggunaan Daya: " + totalPenggunaan + " Watt");
        System.out.println("Perkiraan Biaya Energi: $" + totalBiaya);
    }

    private static void inisialisasiPerangkatDefault() {
        System.out.println("Menginisialisasi perangkat default...");
        perangkat.put("D001", new Perangkat("D001", "Air Conditioner", 2000));
        perangkat.put("D002", new Perangkat("D002", "Refrigerator", 150));
        perangkat.put("D003", new Perangkat("D003", "Electric Heater", 1800));
        perangkat.put("D004", new Perangkat("D004", "Washing Machine", 500));
        perangkat.put("D005", new Perangkat("D005", "Microwave Oven", 1200));
        perangkat.put("D006", new Perangkat("D006", "Television", 100));
        perangkat.put("D007", new Perangkat("D007", "Ceiling Fan", 75));
        perangkat.put("D008", new Perangkat("D008", "Desktop Computer", 300));
        perangkat.put("D009", new Perangkat("D009", "LED Light", 10));
        perangkat.put("D010", new Perangkat("D010", "Water Pump", 750));
    }

    private static boolean muatPerangkat() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PERANGKAT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    perangkat.put(data[0], new Perangkat(data[0], data[1], Integer.parseInt(data[2])));
                }
            }
            return !perangkat.isEmpty();
        } catch (IOException e) {
            System.out.println("Gagal memuat perangkat.");
            return false;
        }
    }

    private static void muatPengguna() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PENGGUNA))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    pengguna.put(data[0], new Pengguna(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat pengguna.");
        }
    }

    private static void simpanPengguna() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PENGGUNA))) {
            for (Pengguna p : pengguna.values()) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan pengguna.");
        }
    }

    private static void simpanPerangkat() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PERANGKAT))) {
            for (Perangkat p : perangkat.values()) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan perangkat.");
        }
    }

    // Getter untuk pengguna
    public static Map<String, Pengguna> getPengguna() {
        return pengguna;
    }
}

class Pengguna {
    private String username;
    private String password;
    private String nama;
    private List<String> notifikasi;

    public Pengguna(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.notifikasi = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getNotifikasi() {
        return notifikasi;
    }

    public String toCSV() {
        return username + "," + password + "," + nama;
    }
}

class Perangkat {
    private String id;
    private String nama;
    private int daya;
    private Map<String, Integer> historiPenggunaan = new HashMap<>(); // Menyimpan histori penggunaan harian (dd/MM/yyyy -> daya)

    // Menambahkan BATAS_DAYA di dalam kelas Perangkat
    private static final int BATAS_DAYA = 1500; // Ambang batas daya (dalam Watt)

    public Perangkat(String id, String nama, int daya) {
        this.id = id;
        this.nama = nama;
        this.daya = daya;
        this.historiPenggunaan.put(LocalDate.now().toString(), daya); // Simpan data penggunaan pertama kali
    }

    public void tambahPenggunaan(int penggunaan) {
        historiPenggunaan.put(LocalDate.now().toString(), penggunaan);
    }

    public void tampilkanHistori() {
        System.out.println("Histori Penggunaan Perangkat " + nama + ":");
        for (Map.Entry<String, Integer> entry : historiPenggunaan.entrySet()) {
            System.out.println("Tanggal: " + entry.getKey() + " - Penggunaan: " + entry.getValue() + " Watt");
        }
    }

    // Menambahkan metode untuk mengecek peringatan daya
    public void cekPeringatanDaya() {
        if (this.daya > BATAS_DAYA) {
            System.out.println("Peringatan! Perangkat " + nama + " menggunakan daya lebih dari " + BATAS_DAYA + " Watt.");
            // Menambahkan notifikasi ke pengguna
            Map<String, Pengguna> penggunaMap = WattMonitorSystem.getPengguna();
            Pengguna pengguna = penggunaMap.get("siska"); // Gantilah "siska" dengan username pengguna yang relevan
            if (pengguna != null) {
                pengguna.getNotifikasi().add("Perangkat " + nama + " melebihi batas daya " + BATAS_DAYA + " Watt.");
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getDaya() {
        return daya;
    }
    
    public void setDaya(int daya) {
        this.daya = daya;
    }
    
    public String toCSV() {
        return id + "," + nama + "," + daya;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Nama: " + nama + ", Daya: " + daya + " Watt";
    }
    
}
