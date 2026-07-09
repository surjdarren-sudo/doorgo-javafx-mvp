package com.doorgo.service;

import com.doorgo.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthService - "penjaga pintu" login & registrasi.

 * MVP: data user disimpan sementara di memori (HashMap), belum pakai database.
 * Di Bab 11 modul OOP (JDBC), penyimpanan ini yang nantinya diganti menjadi
 * tabel `Customer` di database lewat PreparedStatement / DAO pattern.
 */
public class AuthService {

    private static final Map<String, User> REGISTERED_USERS = new HashMap<>();

    static {
        // Akun demo bawaan supaya bisa langsung dicoba tanpa registrasi dulu.
        REGISTERED_USERS.put("pisang", new User("pisang", "Pelanggan DoorGo", "goreng123", 219000));
    }

    /**
     * @return object User jika username & password cocok, atau null jika gagal.
     */
    public static User login(String username, String password) {
        User user = REGISTERED_USERS.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Mendaftarkan akun baru.
     * @return null jika berhasil, atau pesan error jika gagal.
     */
    public static String register(String username, String namaLengkap, String password) {
        if (username.isBlank() || namaLengkap.isBlank() || password.isBlank()) {
            return "Semua kolom wajib diisi.";
        }
        if (REGISTERED_USERS.containsKey(username)) {
            return "Username sudah digunakan.";
        }
        REGISTERED_USERS.put(username, new User(username, namaLengkap, password, 0));
        return null;
    }
}
