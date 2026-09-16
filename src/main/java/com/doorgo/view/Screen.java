package com.doorgo.view;

import javafx.scene.Scene;
import javafx.stage.Stage;


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
