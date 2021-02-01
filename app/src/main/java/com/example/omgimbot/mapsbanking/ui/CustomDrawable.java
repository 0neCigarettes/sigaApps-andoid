package com.example.omgimbot.mapsbanking.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;



public class CustomDrawable {

    public static Drawable googleMaterialDrawable(Context context, int color, int size, GoogleMaterial.Icon icon){
        return new IconicsDrawable(context)
                .color(context.getResources().getColor(color))
                .sizeDp(size)
                .icon(icon);
    }

}
