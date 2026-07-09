package com.doorgo;

import com.doorgo.view.LoginScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Main - titik masuk aplikasi DoorGo (JavaFX MVP).
 *
 * Sesuai modul OOP Bab 2.10: class ini WAJIB extends Application dan
 * meng-override start(Stage). Method main() hanya memanggil launch(args),
 * yang menyalakan runtime JavaFX lalu otomatis memanggil start().
 *
 * Catatan: proses multithreading (javafx.concurrent.Task) sekarang
 * ditaruh di LoginScreen dan RegisterScreen, karena di situlah aksi
 * yang berpotensi lambat (login/registrasi ke server) sebenarnya terjadi -
 * bukan saat aplikasi baru dibuka.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DoorGo - Door to Door Delivery");
        primaryStage.setResizable(false);
        loadAppIcon(primaryStage);

        primaryStage.setScene(new LoginScreen(primaryStage).build());
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * stop() dipanggil otomatis oleh JavaFX saat jendela ditutup.
     * Placeholder untuk cleanup resource (mis. menutup koneksi database
     * begitu fitur JDBC ditambahkan di iterasi berikutnya).
     */
    @Override
    public void stop() {
        System.out.println("DoorGo ditutup. (Tempat untuk cleanup resource nanti.)");
    }

    private void loadAppIcon(Stage stage) {
        try (InputStream is = getClass().getResourceAsStream("/images/logo.png")) {
            if (is != null) {
                stage.getIcons().add(new Image(is));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Terjadi error tak terduga di thread [" + thread.getName() + "]: "
                    + throwable.getMessage());
            throwable.printStackTrace();
        });

        launch(args);
    }
}
