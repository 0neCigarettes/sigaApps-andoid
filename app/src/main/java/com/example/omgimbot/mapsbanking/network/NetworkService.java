package com.example.omgimbot.mapsbanking.network;

import com.example.omgimbot.mapsbanking.ApotekModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by github.com/adip28 on 7/17/2018.
 */
public interface NetworkService {


    @GET("API/getAllDataSpbu")
    Call<List<ApotekModel>> getListBank();


}
