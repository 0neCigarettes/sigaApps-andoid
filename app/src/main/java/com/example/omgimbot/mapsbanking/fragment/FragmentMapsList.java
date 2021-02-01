package com.example.omgimbot.mapsbanking.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.omgimbot.mapsbanking.ApotekModel;
import com.example.omgimbot.mapsbanking.MapsActivity;
import com.example.omgimbot.mapsbanking.R;
import com.example.omgimbot.mapsbanking.ui.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMapsList extends Fragment implements MapsListAdapter.OnItemSelected {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    List<ApotekModel> trackers = new ArrayList<>();
    List<String> dataFilter = new ArrayList<String>();
    private MapsListAdapter adapter;
    private LatLng location = new LatLng(0, 0);

    public static FragmentMapsList newInstance() {
        FragmentMapsList fragment = new FragmentMapsList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_bank_list, container, false);
        ButterKnife.bind(this, rootView);

        if (trackers.size() > 0)
            this.setData(trackers, location);
        return rootView;
    }


    public void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.clearFocus();
    }

    public void setData(List<ApotekModel> trackers, LatLng location) {
        this.trackers = trackers;
        this.location = location;
        if (mRecyclerView != null) {
            if (mRecyclerView.getAdapter() == null) {
                adapter = new MapsListAdapter(getActivity(), this.location, trackers, this);
                mRecyclerView.setAdapter(adapter);
                this.initViews();
            } else {
                adapter.swap(this.trackers, this.location);
            }
        }
    }

    @Override
    public void onSelect(ApotekModel model) {
        LatLng lng = new LatLng(
                Double.parseDouble(model.getLati()),
                Double.parseDouble(model.getLongi())
        );
        ((MapsActivity) getActivity()).hideTrackerList();
        ((MapsActivity) getActivity()).zoomToLng(model.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_view, menu);
        MenuItem searchIem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchIem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                nextText = nextText.toLowerCase();
                ArrayList<ApotekModel> dataFilter = new ArrayList<>();
                for(ApotekModel data : trackers){
                    LatLng from = new LatLng(
                            Double.parseDouble(data.getLati()),
                            Double.parseDouble(data.getLongi())
                    );
                    float distanceInKM = Utils.distanceBetween(from, location);
                    data.setDistance(distanceInKM);
                    String nama = data.getNama().toLowerCase();
                    if(nama.contains(nextText)){
                        dataFilter.add(data);
                        Collections.sort(dataFilter, new ApotekModel.Sortbyroll());
                    }
                }
                adapter.setFilter(dataFilter);
                return true;
            }
        });

    }


}
