package com.example.omgimbot.mapsbanking.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.omgimbot.mapsbanking.ApotekModel;
import com.example.omgimbot.mapsbanking.R;
import com.example.omgimbot.mapsbanking.ui.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MapsListAdapter extends RecyclerView.Adapter<MapsListAdapter.ViewHolder> {
    private List<ApotekModel> trackers;
    private OnItemSelected listener;
    private int color;
    private Context context;
    private LatLng location;

    public interface OnItemSelected {
        void onSelect(ApotekModel model);
    }

    public MapsListAdapter(Context context, LatLng location, List<ApotekModel> trackers, OnItemSelected listener) {
        this.trackers = trackers;
        this.listener = listener;
        this.context = context;
        this.location = location;
    }

    public void swap(List<ApotekModel> datas, LatLng location) {
        if (datas == null || datas.size() == 0)
            return;
        if (trackers != null && trackers.size() > 0)
            trackers.clear();
        trackers.addAll(datas);
        this.location = location;
        notifyDataSetChanged();

    }

    void setFilter(ArrayList<ApotekModel> filterList){
        trackers = new ArrayList<>();
        trackers.addAll(filterList);
        notifyDataSetChanged();
    }

    @Override
    public MapsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_dialogs, parent, false);
        MapsListAdapter.ViewHolder viewHolder = new MapsListAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MapsListAdapter.ViewHolder holder, final int position) {
        final ApotekModel tracker = trackers.get(position);
        String cityName = "";
        holder.mAgo.setVisibility(View.GONE);
        holder.mTime.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(view -> listener.onSelect(tracker));
        holder.mName.setText(tracker.getNama());
        holder.mAddress.setText(tracker.getAlamat());
        LatLng from = new LatLng(
                Double.parseDouble(tracker.getLati()),
                Double.parseDouble(tracker.getLongi())
        );
        if(location.latitude != 0 && location.longitude != 0) {
            float distanceInKM = Utils.distanceBetween(from, location);
            String rich = "Sekitar <b>" + distanceInKM + "KM</b> dari Lokasi Anda";
            holder.mDistance.setText(Html.fromHtml(rich));
        }
        holder.iconImg.setImageResource(R.drawable.apotek_icon);

    }


    @Override
    public int getItemCount() {
        return trackers.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mName, mDistance, mAddress, mAgo, mTime;
        ImageView iconImg;

        ViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.plat_text);
            mAddress = view.findViewById(R.id.name_text);
            mDistance = view.findViewById(R.id.distance_text);
            mAgo = view.findViewById(R.id.ago_text);
            mTime = view.findViewById(R.id.time_text);
            iconImg = view.findViewById(R.id.icon_image);
        }
    }


}
