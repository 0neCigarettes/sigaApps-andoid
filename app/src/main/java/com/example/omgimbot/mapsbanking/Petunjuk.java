package com.example.omgimbot.mapsbanking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Petunjuk extends AppCompatActivity {

    TextView txtPetunjuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petunjuk);

        txtPetunjuk = (TextView) findViewById(R.id.txtPetunjuk);

        String petunjuk = "     Pilih menu utama, setiap icon akan menampilkan gambaran menu yang dipilih.\n" +
                "1. Di menu apotek pengguna dapat menginput apotek yang akan di cari, dan daftar nama-nama apotek serta peta apotek yang dicari.\n" +
                "2. Di menu peta pengguna dapat melihat peta apotek di bandar Lampung.\n" +
                "3. Di menu Petunjuk pengguna dapat melihat bagai mana menggunakan aplikasi ini.\n" +
                "4. Di menu tentang pengguna dapat melihat penjelasan aplikasi dan biodata pembuat aplikasi.";

        txtPetunjuk.setText(petunjuk);
    }

    public void onBackPressed() {
        Intent i = new Intent(Petunjuk.this, home.class);
        startActivity(i);
        finish();
    }
}