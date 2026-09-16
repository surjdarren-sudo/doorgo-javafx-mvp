package com.doorgo.model;

/**
 * Fitur - merepresentasikan satu layanan/produk DoorGo yang tampil
 * sebagai kartu di halaman Home (contoh: Live Tracking, Contactless Delivery, dst).
 */
public class Fitur {

    private final String judul;
    private final String deskripsi;
    private final String emoji; 
    private final String warnaHex;

    public Fitur(String judul, String deskripsi, String emoji, String warnaHex) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.emoji = emoji;
        this.warnaHex = warnaHex;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getWarnaHex() {
        return warnaHex;
    }
}
