package com.doorgo.model;

/**
 * User - merepresentasikan pelanggan yang login/registrasi ke aplikasi DoorGo.
 *
 * Menerapkan ENCAPSULATION: seluruh field bersifat private, hanya bisa
 * diakses/diubah lewat getter dan setter publik.
 */
public class User {

    private String username;
    private String namaLengkap;
    private String password;
    private double saldo;

    public User(String username, String namaLengkap, String password, double saldo) {
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.password = password;
        this.saldo = saldo;
    }

    public String getUsername() {
        return username;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getPassword() {
        return password;
    }

    public double getSaldo() {
        return saldo;
    }

    
    public void tambahSaldo(double jumlah) {
        if (jumlah > 0) {
            this.saldo += jumlah;
        }
    }
}
