package com.doorgo.view;

import com.doorgo.model.User;
import com.doorgo.service.AuthService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * LoginScreen - halaman pertama yang dilihat pelanggan.
 * Menampilkan logo DoorGo, form username/password, dan tombol "Sign In".
 *
 * MULTITHREADING: proses login dijalankan lewat javafx.concurrent.Task di
 * background thread, supaya kalau nanti AuthService.login() sudah terhubung
 * ke server/database sungguhan (bisa lambat), jendela aplikasi tetap
 * responsif (tidak freeze) selama menunggu.
 */
public class LoginScreen extends Screen {

    public LoginScreen(Stage stage) {
        super(stage);
    }

    @Override
    public Scene build() {
        ImageView logoView = loadLogo();

        Label appName = new Label("DoorGo");
        appName.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        appName.setTextFill(Color.web("#7B2FF7"));

        Label tagline = new Label("Door to Door Delivery");
        tagline.setFont(Font.font("Verdana", 13));
        tagline.setTextFill(Color.web("#888888"));

        Label userLabel = new Label("Username");
        TextField userField = new TextField();
        userField.setPromptText("Masukkan username");
        userField.setMaxWidth(260);

        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Masukkan password");
        passField.setMaxWidth(260);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#E63946"));

        Button signInButton = new Button("Sign In");
        signInButton.setPrefWidth(260);
        signInButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 0 10 0;"
        );

        signInButton.setOnAction(event -> {
            String username = userField.getText().trim();
            String password = passField.getText();
            errorLabel.setText("");

            // Jalankan proses login di background thread lewat Task,
            // supaya UI (tombol, field) tetap responsif selama diproses.
            runLoginTask(username, password, signInButton, errorLabel);
        });

        Label hint = new Label("Demo login -> username: pisang | password: goreng123");
        hint.setFont(Font.font("Verdana", 10));
        hint.setTextFill(Color.web("#aaaaaa"));

        Label registerLink = new Label("Don't have an account? Register");
        registerLink.setTextFill(Color.web("#2196F3"));
        registerLink.setOnMouseClicked(e -> goTo(new RegisterScreen(stage)));

        VBox formBox = new VBox(10, userLabel, userField, passLabel, passField,
                errorLabel, signInButton, hint, registerLink);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(280);

        VBox root = new VBox(15, logoView, appName, tagline, formBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f4f6;");

        return new Scene(root, 380, 640);
    }

    /**
     * Membungkus AuthService.login() ke dalam Task agar berjalan di
     * background thread. Callback setOnSucceeded()/setOnFailed() otomatis
     * dijalankan kembali di JavaFX Application Thread, jadi aman untuk
     * mengubah tampilan (pindah scene, ubah teks) langsung dari situ.
     */
    private void runLoginTask(String username, String password, Button signInButton, Label errorLabel) {
        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() {
                // Simulasi proses yang berpotensi lambat (nanti diganti
                // query database sungguhan lewat JDBC).
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                return AuthService.login(username, password);
            }
        };

        // Nonaktifkan tombol & beri feedback visual selagi Task berjalan.
        signInButton.setDisable(true);
        signInButton.setText("Memproses...");

        loginTask.setOnSucceeded(e -> {
            signInButton.setDisable(false);
            signInButton.setText("Sign In");

            User user = loginTask.getValue();
            if (user != null) {
                goTo(new HomeScreen(stage, user));
            } else {
                errorLabel.setText("Username atau password salah. Coba lagi.");
            }
        });

        loginTask.setOnFailed(e -> {
            signInButton.setDisable(false);
            signInButton.setText("Sign In");
            errorLabel.setText("Terjadi kesalahan, silakan coba lagi.");
            loginTask.getException().printStackTrace();
        });

        Thread thread = new Thread(loginTask, "doorgo-login-thread");
        thread.setDaemon(true);
        thread.start();
    }

    private ImageView loadLogo() {
        ImageView view;
        try (InputStream is = getClass().getResourceAsStream("/images/logo.png")) {
            if (is != null) {
                view = new ImageView(new Image(is));
                view.setFitWidth(120);
                view.setFitHeight(120);
                view.setPreserveRatio(true);
                return view;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = new ImageView();
        return view;
    }
}
