package com.example.omgimbot.mapsbanking.ui;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omgimbot.mapsbanking.ApotekModel;
import com.example.omgimbot.mapsbanking.R;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.maps.model.LatLng;


public class BottomDialogs {

    public BottomDialogs() {

    }

    public interface onDialogClose {
        void onDismiss(String string);

    }

    public interface onPlaceSelected {
        void onDismiss(LatLng pos, String address);
    }





    public static void showTrackerDetail(Activity activity, String title, ApotekModel apotekModel, LatLng location){
        String cityName = "";
        double lat = Double.parseDouble(apotekModel.getLati());
        double lang = Double.parseDouble(apotekModel.getLongi());
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.item_bottom_dialogs, null);
        TextView PlatText = customView.findViewById(R.id.plat_text);
        TextView AgoText = customView.findViewById(R.id.ago_text);
        AgoText.setVisibility(View.GONE);
        TextView TimeText = customView.findViewById(R.id.time_text);
        TimeText.setVisibility(View.GONE);
        TextView NameText = customView.findViewById(R.id.name_text);
        ImageView IconImage = customView.findViewById(R.id.icon_image);
        View Line = customView.findViewById(R.id.line_view);
        TextView DistanceText = customView.findViewById(R.id.distance_text);
        LatLng from = new LatLng(
                Double.parseDouble(apotekModel.getLati()),
                Double.parseDouble(apotekModel.getLongi())
        );
        if(location.latitude != 0 && location.longitude != 0) {
            float distanceInKM = Utils.distanceBetween(from, location);
            String rich = "Sekitar <b>" + distanceInKM + "KM</b> dari Lokasi Anda";
            DistanceText.setText(Html.fromHtml(rich));
        }

        IconImage.setImageResource(R.drawable.apotek_icon);
        IconImage.setVisibility(View.GONE);
        PlatText.setText(apotekModel.getDeskripsi());
//        AgoText.setText(Utils.convertMongoDateToAgo(tracker.getTime()));
//        TimeText.setText("Pada "+Utils.convertMongoDate(tracker.getTime()));
        //NameText.setText(tracker.getName());
        NameText.setText(apotekModel.getAlamat());

        BottomDialog dialog = new BottomDialog.Builder(activity)
                .setTitle(apotekModel.getNama())
                .setIcon(IconImage.getDrawable())
                .setCancelable(true)
                .setCustomView(customView)
                .build();
        dialog.show();
    }


}
