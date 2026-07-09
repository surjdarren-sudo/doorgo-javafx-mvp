package com.doorgo.view;

import com.doorgo.model.Fitur;
import com.doorgo.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

/**
 * HomeScreen - halaman utama setelah login.
 * Menampilkan sapaan pelanggan, saldo, dan daftar produk/fitur DoorGo
 * (Live Tracking, Contactless Delivery, ETA Akurat, Chat/Telpon Kurir,
 * Feedback & Tip) dalam bentuk kartu, sesuai fitur pada Laporan ISAD.
 */
public class HomeScreen extends Screen {

    private final User user;

    public HomeScreen(Stage stage, User user) {
        super(stage);
        this.user = user;
    }

    @Override
    public Scene build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffffff;");

        root.setTop(buildHeader());
        root.setCenter(buildFeatureGrid());
        root.setBottom(buildBottomNav());

        return new Scene(root, 380, 700);
    }

    // ---------- Header: sapaan + saldo ----------
    private VBox buildHeader() {
        Label greeting = new Label("Halo, " + user.getNamaLengkap() + " 👋");
        greeting.setStyle("-fx-font-family: 'Verdana'; -fx-font-weight: bold; -fx-font-size: 16px;");
        greeting.setTextFill(Color.WHITE);

        Label saldoLabel = new Label(String.format("Rp%,.0f", user.getSaldo()));
        saldoLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        saldoLabel.setTextFill(Color.WHITE);

        Label saldoCaption = new Label("Saldo DoorGo Wallet");
        saldoCaption.setFont(Font.font("Verdana", 11));
        saldoCaption.setTextFill(Color.web("#f0e6ff"));

        VBox headerBox = new VBox(4, greeting, saldoCaption, saldoLabel);
        headerBox.setPadding(new Insets(30, 20, 25, 20));
        headerBox.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #ff6fa5, #7b2ff7);"
        );
        return headerBox;
    }

    // ---------- Grid kartu fitur/produk ----------
    private ScrollPane buildFeatureGrid() {
        List<Fitur> fiturList = List.of(
                new Fitur("Live Tracking", "Pantau posisi kurir secara real-time di peta", "📍", "#FFE3EC"),
                new Fitur("Contactless Delivery", "Bukti kirim digital: foto & tanda tangan", "📦", "#E3F0FF"),
                new Fitur("Estimasi Akurat", "ETA berdasarkan lalu lintas & lokasi kurir", "⏱️", "#FFF6E0"),
                new Fitur("Chat & Telepon", "Hubungi kurir langsung dari aplikasi", "💬", "#E9F9EE"),
                new Fitur("Rating & Tip", "Beri feedback dan tip digital ke kurir", "⭐", "#F3E9FF")
        );

        FlowPane flow = new FlowPane();
        flow.setHgap(14);
        flow.setVgap(14);
        flow.setPadding(new Insets(20));
        flow.setAlignment(Pos.TOP_CENTER);

        Label sectionTitle = new Label("Layanan DoorGo");
        sectionTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        VBox container = new VBox(10, sectionTitle, flow);
        container.setPadding(new Insets(10, 15, 10, 15));

        for (Fitur fitur : fiturList) {
            flow.getChildren().add(buildFiturCard(fitur));
        }

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: white; -fx-background-color: transparent;");
        return scroll;
    }

    private VBox buildFiturCard(Fitur fitur) {
        Label icon = new Label(fitur.getEmoji());
        icon.setStyle("-fx-font-family: 'Segoe UI Emoji', 'Segoe UI Symbol'; -fx-font-size: 26px;");

        Label title = new Label(fitur.getJudul());
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        title.setWrapText(true);

        Label desc = new Label(fitur.getDeskripsi());
        desc.setFont(Font.font("Verdana", 9));
        desc.setTextFill(Color.web("#666666"));
        desc.setWrapText(true);

        VBox card = new VBox(6, icon, title, desc);
        card.setPadding(new Insets(14));
        card.setPrefWidth(155);
        card.setPrefHeight(120);
        card.setStyle(
                "-fx-background-color: " + fitur.getWarnaHex() + "; -fx-background-radius: 14;"
        );

        // Interaksi klik kartu -> menampilkan detail (MVP: pakai Alert dulu,
        // di versi penuh ini akan goTo(new TrackingScreen(...)) dst.)
        card.setOnMouseClicked(e -> showFiturDetail(fitur));

        return card;
    }

    private void showFiturDetail(Fitur fitur) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(fitur.getJudul());
        alert.setHeaderText(fitur.getEmoji() + " " + fitur.getJudul());
        alert.setContentText(fitur.getDeskripsi() + "\n\n(Fitur ini akan dikembangkan lebih lanjut "
                + "menjadi layar tersendiri, contoh: TrackingScreen dengan peta live.)");
        alert.showAndWait();
    }

    // ---------- Bottom navigation sederhana ----------
    private HBox buildBottomNav() {
        Label home = navItem("🏠", "Home");
        Label promo = navItem("🏷️", "Promo");
        Label order = navItem("🧾", "Pesanan");
        Label chat = navItem("💬", "Chat");

        HBox nav = new HBox(home, promo, order, chat);
        nav.setAlignment(Pos.CENTER);
        nav.setSpacing(30);
        nav.setPadding(new Insets(12));
        nav.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
        return nav;
    }

    private Label navItem(String emoji, String label) {
        Label l = new Label(emoji + "\n" + label);
        l.setStyle("-fx-font-family: 'Segoe UI Emoji', 'Verdana'; -fx-font-size: 10px; -fx-text-alignment: center;");
        return l;
    }
}
