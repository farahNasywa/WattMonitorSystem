# WattMonitorSystem


WattMonitorSystem adalah aplikasi berbasis Java untuk memantau penggunaan daya listrik perangkat rumah tangga, yang dirancang untuk meningkatkan efisiensi energi dan mencegah penggunaan daya berlebih.

## Fitur Utama
1. **Manajemen Pengguna**:
   - Registrasi pengguna baru.
   - Login sebagai admin atau pengguna biasa.
2. **Manajemen Perangkat** (Admin):
   - Menambahkan perangkat baru.
   - Mengedit perangkat yang sudah ada.
   - Melihat daftar perangkat yang terdaftar.
3. **Laporan Penggunaan Energi**:
   - Menampilkan total daya yang digunakan dan perkiraan biaya energi berdasarkan tarif per kWh.
4. **Notifikasi**:
   - Memberikan peringatan jika daya perangkat melebihi batas daya yang telah ditentukan (1500 Watt).
5. **Histori Penggunaan Perangkat**:
   - Melacak penggunaan daya perangkat setiap hari.
6. **Penyimpanan Data**:
   - Data pengguna disimpan di file `pengguna.txt`.
   - Data perangkat disimpan di file `perangkat.txt`.

## Persyaratan Sistem
- **Java**: Minimal Java 8
- **Lingkungan Pengembangan**: Editor teks atau IDE seperti IntelliJ IDEA atau Eclipse.

## Cara Menjalankan
1. **Clone atau Unduh Repository**:
   ```bash
   git clone https://github.com/username/repository.git
   ```
2. **Kompilasi dan Jalankan Program**:
   - Kompilasi:
     ```bash
     javac WattMonitorSystem.java
     ```
   - Jalankan:
     ```bash
     java WattMonitorSystem
     ```
3. **Masuk ke Aplikasi**:
   - Gunakan kredensial admin default: 
     ```
     Username: admin
     Password: admin123
     ```
   - Untuk pengguna baru, lakukan registrasi terlebih dahulu.

## Struktur File
- `WattMonitorSystem.java`: File utama aplikasi.
- `Pengguna.java`: Kelas untuk manajemen pengguna.
- `Perangkat.java`: Kelas untuk manajemen perangkat dan histori penggunaan.
- `pengguna.txt`: File penyimpanan data pengguna.
- `perangkat.txt`: File penyimpanan data perangkat.

## Mode Admin
1. Tambah perangkat baru.
2. Edit perangkat yang sudah ada.
3. Lihat semua perangkat yang terdaftar.

## Mode Pengguna
1. Lihat perangkat terdaftar.
2. Terima notifikasi penggunaan daya.
3. Lihat laporan penggunaan energi.
4. Hapus semua notifikasi.

## Tarif Listrik
- Tarif listrik default adalah **$0.15 per kWh**.
- Dapat dimodifikasi dalam kode dengan mengubah nilai `BIAYA_PER_KWH`.

## Ambang Batas Daya
- Ambang batas daya default adalah **1500 Watt**.
- Perangkat yang melebihi batas ini akan memicu peringatan.

