# DoorGo — JavaFX MVP

Implementasi GUI awal (MVP) dari **Laporan ISAD DoorGo** (Kelompok Pisang Goreng),
dibangun mengikuti pola OOP dari **Modul Pembelajaran OOP Java** (Class & Object,
Encapsulation, Inheritance, Polymorphism, Abstraction, JavaFX).

Yang sudah ada di MVP ini:
1. **Login Screen** — logo DoorGo + form username/password + validasi sederhana.
2. **Register Screen** — form pendaftaran akun baru, setelah berhasil otomatis kembali ke Login.
3. **Home Screen** — sapaan pelanggan, saldo, dan kartu 5 fitur utama (Live Tracking,
   Contactless Delivery, Estimasi Akurat, Chat & Telepon Kurir, Rating & Tip),
   sesuai daftar fitur di Laporan ISAD.
4. Struktur kode berbasis 4 pilar OOP (lihat bagian "Kaitan dengan Modul OOP" di bawah).

Yang **belum** dibuat (di luar cakupan MVP, langkah lanjutan disebut di akhir dokumen):
tracking peta sungguhan, database (JDBC), registrasi akun, chat real-time, pembayaran.

---

## 1. Panduan Instalasi JavaFX & Rekomendasi IDE

### Rekomendasi IDE: **IntelliJ IDEA (Community Edition, gratis)**
Alasan:
- Dukungan Maven bawaan sangat baik — kamu tidak perlu men-download JavaFX SDK
  secara manual, Maven yang otomatis mengunduh library JavaFX lewat `pom.xml`.
- Auto-complete, refactoring, dan run-configuration untuk Maven goals sangat mulus.
- Cross-platform (Windows/Mac/Linux), cocok kalau anggota kelompok pakai OS berbeda.

Alternatif yang juga bisa dipakai:
- **NetBeans** — pernah punya plugin JavaFX bawaan lebih lama, cocok kalau kamu
  sudah terbiasa dari mata kuliah (banyak contoh di modul memakai gaya NetBeans).
- **VS Code** + extension "Extension Pack for Java" — ringan, tapi setup Maven run
  sedikit lebih manual dibanding IntelliJ.

> Proyek ini pakai **Maven**, jadi apa pun IDE-nya, langkah instalasi JavaFX-nya sama:
> Maven yang mengurus dependency JavaFX, kamu tidak perlu download JavaFX SDK zip
> secara terpisah dari gluon.io.

### Langkah instalasi (step-by-step)

1. **Install JDK 17 atau lebih baru**
   - Download dari [Eclipse Temurin](https://adoptium.net/) (rekomendasi, gratis, cross-platform)
     atau Oracle JDK.
   - Cek instalasi lewat terminal:
     ```
     java -version
     ```
     Pastikan versinya 17 ke atas.

2. **Install Maven** (kalau IDE belum membawanya sendiri)
   - IntelliJ IDEA sudah membawa Maven bawaan (Bundled Maven), jadi biasanya
     **tidak perlu install manual**.
   - Kalau mau pakai terminal murni, download dari [maven.apache.org](https://maven.apache.org/download.cgi)
     lalu cek dengan `mvn -version`.

3. **Install IntelliJ IDEA Community Edition**
   - Download dari [jetbrains.com/idea/download](https://www.jetbrains.com/idea/download/)
     (pilih tab Community).
   - Saat instalasi, centang opsi "Add launchers dir to PATH" agar bisa dibuka dari terminal.

4. **Buka proyek**
   - Extract folder `doorgo-javafx-mvp` (folder yang berisi file ini).
   - Buka IntelliJ IDEA → `Open` → pilih folder `doorgo-javafx-mvp` (folder yang
     berisi `pom.xml`).
   - IntelliJ akan otomatis mendeteksi ini sebagai proyek Maven dan mulai
     mengunduh dependency JavaFX (`javafx-controls`, `javafx-graphics`) dari
     Maven Central. **Tunggu sampai proses "Resolving dependencies" selesai**
     (butuh koneksi internet, sekali saja — setelah itu tersimpan di cache lokal).

5. **Jalankan aplikasi**
   - Cara termudah: buka panel Maven di sisi kanan IntelliJ → `doorgo-javafx-mvp`
     → `Plugins` → `javafx` → double click `javafx:run`.
   - Atau lewat terminal, di root folder proyek:
     ```
     mvn clean javafx:run
     ```
   - Jendela aplikasi DoorGo akan muncul, dimulai dari Login Screen.

6. **Login demo**
   - Username: `pisang`
   - Password: `goreng123`
   - Setelah berhasil, akan pindah otomatis ke Home Screen berisi kartu-kartu fitur.

7. **Coba alur Register**
   - Dari Login Screen, klik teks "Don't have an account? Register".
   - Isi Nama, Username, Password, Konfirmasi Password lalu klik "Register".
   - Muncul alert sukses, lalu otomatis kembali ke Login Screen — login pakai
     akun yang baru saja dibuat.

### Troubleshooting umum
| Masalah | Penyebab | Solusi |
|---|---|---|
| `Error: JavaFX runtime components are missing` | Menjalankan `.jar` langsung lewat `java -jar` tanpa modul JavaFX | Gunakan `mvn javafx:run`, bukan `java -jar`, karena plugin ini yang menyuntikkan module JavaFX otomatis |
| Maven gagal download dependency | Tidak ada koneksi internet / firewall kampus | Sambungkan ke internet lain, atau minta admin membuka akses ke `repo.maven.apache.org` |
| Logo tidak muncul | File `logo.png` tidak ikut ter-copy ke `target/classes` | Jalankan `mvn clean javafx:run` (bukan cuma `javafx:run`) supaya resource di-refresh |

---

## 2. Struktur Proyek

```
doorgo-javafx-mvp/
├── pom.xml                          <- konfigurasi Maven + dependency JavaFX
├── README.md                        <- dokumen ini
└── src/main/
    ├── java/com/doorgo/
    │   ├── Main.java                <- entry point (extends Application)
    │   ├── model/
    │   │   ├── User.java            <- data pelanggan (ENCAPSULATION)
    │   │   └── Fitur.java           <- data 1 kartu fitur/produk
    │   ├── service/
    │   │   └── AuthService.java     <- logika validasi login (belum pakai DB)
    │   └── view/
    │       ├── Screen.java          <- abstract class (ABSTRACTION + kontrak build())
    │       ├── LoginScreen.java     <- extends Screen (INHERITANCE)
    │       ├── RegisterScreen.java  <- extends Screen (INHERITANCE)
    │       └── HomeScreen.java      <- extends Screen (INHERITANCE, POLYMORPHISM)
    └── resources/images/
        └── logo.png                 <- logo DoorGo yang tampil di Login Screen
```

---

## 3. Kaitan dengan Modul OOP (agar mudah dijelaskan di laporan/presentasi)

| Konsep OOP (dari Modul) | Implementasi di Proyek Ini |
|---|---|
| **Encapsulation** | `User.java` — field `saldo` bersifat `private`, hanya bisa ditambah lewat method `tambahSaldo()` yang tervalidasi (mirip `RekeningBank` di Bab 4 modul). |
| **Abstraction** | `Screen.java` — abstract class dengan method abstrak `build()`, sama seperti `GameObject` di Bab 13 modul yang mewajibkan subclass menyediakan `render()`. |
| **Inheritance** | `LoginScreen` dan `HomeScreen` sama-sama `extends Screen`, mewarisi field `stage` dan method `goTo()`. |
| **Polymorphism** | Setiap subclass Screen meng-override `build()` dengan tampilan berbeda — dipanggil lewat referensi tipe `Screen` yang sama, persis seperti `render()` pada `Player` vs `Enemy` di Bab 13. |
| **Event-Driven Programming** | `signInButton.setOnAction(event -> {...})` di `LoginScreen` — lambda ini adalah implementasi ringkas dari `EventHandler<ActionEvent>.handle()`, dijelaskan di Bab 2.9 & 10 modul. |
| **`main()` + `launch()`** | `Main.java` mengikuti pola persis Bab 2.10: `main()` cuma memanggil `launch(args)`, lalu JavaFX otomatis memanggil `start(Stage)`. |

---

## 4. Langkah Lanjutan (Roadmap dari MVP ke Versi Penuh)

Sesuai *System Request* di Laporan ISAD, urutan pengembangan yang disarankan:

1. **Tambah `RegisterScreen`** — extends `Screen`, form registrasi pelanggan baru.
2. **Sambungkan ke database (JDBC)** — ganti `AuthService.login()` yang sekarang
   hardcode, menjadi query `SELECT` ke tabel `Customer` lewat `PreparedStatement`
   (contoh pola lengkap ada di Bab 11 & 12 modul OOP — `DatabaseConnector`, DAO pattern).
3. **`TrackingScreen`** — tampilkan peta (bisa mulai dari placeholder `Canvas`,
   nanti diintegrasikan ke API Maps sesuai rekomendasi arsitektur di Laporan ISAD).
4. **`ChatScreen`** — form chat sederhana ke kurir, memakai pola `ListView` + `TextField`.
5. **`FeedbackScreen`** — rating bintang + kolom tip, terhubung ke tabel `Feedback`.
6. Terapkan pola **DAO** (Bab 11.5 modul) supaya class `view` tidak berisi SQL langsung —
   pisahkan ke `CustomerDAO`, `OrderDAO`, dst. agar sesuai prinsip *Single Responsibility*.

Setiap layar baru cukup dibuat sebagai `class XxxScreen extends Screen`, meng-override
`build()`, lalu dipanggil dari layar lain lewat `goTo(new XxxScreen(stage, ...))` —
pola yang sudah dipakai di `LoginScreen -> HomeScreen`.
