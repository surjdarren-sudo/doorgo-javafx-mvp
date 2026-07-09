package com.doorgo.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Screen - "cetak biru" untuk setiap layar/halaman aplikasi DoorGo.
 *
 * Menerapkan ABSTRACTION: kelas ini tidak tahu (dan tidak peduli) detail
 * tampilan tiap halaman, ia hanya mewajibkan setiap turunannya (subclass)
 * menyediakan method build() yang mengembalikan Scene siap-pakai.
 *
 * LoginScreen dan HomeScreen adalah contoh INHERITANCE: keduanya
 * extends Screen dan mewarisi field `stage`, lalu meng-override build()
 * dengan caranya masing-masing -> ini juga contoh POLYMORPHISM,
 * sama seperti method render() pada GameObject/Player/Enemy di modul OOP.
 */
public abstract class Screen {

    protected final Stage stage;

    protected Screen(Stage stage) {
        this.stage = stage;
    }

    /**
     * Membangun tampilan (Scene) untuk layar ini.
     * Wajib di-override oleh setiap subclass.
     */
    public abstract Scene build();

    /**
     * Helper agar setiap screen bisa berpindah ke screen lain
     * dengan satu baris kode: goTo(new HomeScreen(stage, user));
     */
    protected void goTo(Screen nextScreen) {
        stage.setScene(nextScreen.build());
    }
}
