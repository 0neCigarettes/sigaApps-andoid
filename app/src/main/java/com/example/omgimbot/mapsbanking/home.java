package com.example.omgimbot.mapsbanking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class home extends AppCompatActivity {


    LinearLayout btnMap, btnApotek, btnAbout, btnPetunjuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        btnMap = (LinearLayout) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this,MapsActivity.class);
                i.putExtra("map", "maps");
                startActivity(i);
                finish();
            }
        });

        btnApotek = (LinearLayout) findViewById(R.id.btnApotek);
        btnApotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this,MapsActivity.class);
                i.putExtra("apotek", "apoteks");
                startActivity(i);
                finish();
            }
        });

        btnAbout = (LinearLayout) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this,about.class);
                startActivity(i);
                finish();
            }
        });

        btnPetunjuk = (LinearLayout) findViewById(R.id.btnPetunjuk);
        btnPetunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this,Petunjuk.class);
                startActivity(i);
                finish();
            }
        });


    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}