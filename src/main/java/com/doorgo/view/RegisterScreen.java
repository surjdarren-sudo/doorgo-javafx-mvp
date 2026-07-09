package com.doorgo.view;

import com.doorgo.service.AuthService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * RegisterScreen - form pendaftaran akun baru.
 * Setelah berhasil register, otomatis kembali ke LoginScreen.
 *
 * MULTITHREADING: sama seperti LoginScreen, proses registrasi dibungkus
 * Task agar berjalan di background thread dan tidak memblokir tampilan.
 */
public class RegisterScreen extends Screen {

    public RegisterScreen(Stage stage) {
        super(stage);
    }

    @Override
    public Scene build() {
        Label title = new Label("Buat Akun DoorGo");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#7B2FF7"));

        TextField namaField = new TextField();
        namaField.setPromptText("Nama lengkap");
        namaField.setMaxWidth(260);

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(260);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(260);

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Konfirmasi password");
        confirmField.setMaxWidth(260);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#E63946"));
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(260);

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(260);
        registerButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 0 10 0;"
        );

        registerButton.setOnAction(event -> {
            String nama = namaField.getText().trim();
            String username = userField.getText().trim();
            String password = passField.getText();
            String confirm = confirmField.getText();
            errorLabel.setText("");

            if (!password.equals(confirm)) {
                errorLabel.setText("Password dan konfirmasi tidak cocok.");
                return;
            }

            runRegisterTask(username, nama, password, registerButton, errorLabel);
        });

        Label backLink = new Label("Sudah punya akun? Login di sini");
        backLink.setTextFill(Color.web("#2196F3"));
        backLink.setOnMouseClicked(e -> goTo(new LoginScreen(stage)));

        VBox formBox = new VBox(10, namaField, userField, passField, confirmField,
                errorLabel, registerButton, backLink);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(280);

        VBox root = new VBox(20, title, formBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f4f6;");

        return new Scene(root, 380, 640);
    }

    /**
     * Membungkus AuthService.register() ke dalam Task agar berjalan di
     * background thread, dengan pola yang sama seperti runLoginTask()
     * di LoginScreen.
     */
    private void runRegisterTask(String username, String nama, String password,
                                  Button registerButton, Label errorLabel) {
        Task<String> registerTask = new Task<>() {
            @Override
            protected String call() {
                try {
                    Thread.sleep(400); // simulasi proses menyimpan ke server/database
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                return AuthService.register(username, nama, password); // null = sukses
            }
        };

        registerButton.setDisable(true);
        registerButton.setText("Memproses...");

        registerTask.setOnSucceeded(e -> {
            registerButton.setDisable(false);
            registerButton.setText("Register");

            String error = registerTask.getValue();
            if (error != null) {
                errorLabel.setText(error);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registrasi Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Akun berhasil dibuat. Silakan login.");
            alert.showAndWait();

            goTo(new LoginScreen(stage));
        });

        registerTask.setOnFailed(e -> {
            registerButton.setDisable(false);
            registerButton.setText("Register");
            errorLabel.setText("Terjadi kesalahan, silakan coba lagi.");
            registerTask.getException().printStackTrace();
        });

        Thread thread = new Thread(registerTask, "doorgo-register-thread");
        thread.setDaemon(true);
        thread.start();
    }
}
