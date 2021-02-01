package com.example.omgimbot.mapsbanking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.omgimbot.mapsbanking.R.id.txtAbout;

public class about extends AppCompatActivity {

    TextView txtAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtAbout = (TextView) findViewById(R.id.txtAbout);

        String tentang = "      Aplikasi apotek kota bandar Lampung\n" +
                "\n" +
                "   Aplikasi apotek kota bandar Lampung adalah aplikasi pencarian yg berfungsi untuk memudahkan pengguna dalam mencari atau menemukan lokasi dan informasi tentang apotek terdekat dengan posisi pengguna yang ada di bandar Lampung.\n" +
                "Diharapkan aplikasi ini akan bermanfaat bagi siapa saja yang membutuhkan.\n" +
                "\n" +
                "Pengembang:\n" +
                "Nama   : Siti purniyawati\n" +
                "Npm    : 14421029\n" +
                "Email  : purniavirgo@gmail.com\n" +
                "Universitas Bandar Lampung";

        txtAbout.setText(tentang);

    }

    public void onBackPressed() {
        Intent i = new Intent(about.this, home.class);
        startActivity(i);
        finish();
    }
}