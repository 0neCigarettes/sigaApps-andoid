package com.example.omgimbot.mapsbanking;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by omgimbot on 1/29/2019.
 */

public class ApotekModel {
    @SerializedName("id")
    private String id ;

    @SerializedName("nama")
    private String nama ;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("kd_kec")
    private String kd_kec ;

    @SerializedName("deskripsi")
    private String deskripsi ;

    @SerializedName("gambar")
    private String gambar ;

    @SerializedName("lati")
    private String lati ;

    @SerializedName("longi")
    private String longi ;

    private float distance;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKd_kec() {
        return kd_kec;
    }

    public void setKd_kec(String kd_kec) {
        this.kd_kec = kd_kec;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public static class Sortbyroll implements Comparator<ApotekModel>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(ApotekModel a, ApotekModel b)
        {
            //return (int) (a.getDistance() - b.getDistance());
            return Float.valueOf(a.getDistance()).compareTo(Float.valueOf(b.getDistance()));
        }
    }
}
