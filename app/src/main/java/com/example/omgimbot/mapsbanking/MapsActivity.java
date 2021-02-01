package com.example.omgimbot.mapsbanking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.omgimbot.mapsbanking.direction.helper.GsonRequest;
import com.example.omgimbot.mapsbanking.direction.helper.Helper;
import com.example.omgimbot.mapsbanking.direction.helper.VolleySingleton;
import com.example.omgimbot.mapsbanking.direction.model.DirectionObject;
import com.example.omgimbot.mapsbanking.direction.model.LegsObject;
import com.example.omgimbot.mapsbanking.direction.model.PolylineObject;
import com.example.omgimbot.mapsbanking.direction.model.RouteObject;
import com.example.omgimbot.mapsbanking.direction.model.StepsObject;
import com.example.omgimbot.mapsbanking.fragment.FragmentMapsList;
import com.example.omgimbot.mapsbanking.network.NetworkService;
import com.example.omgimbot.mapsbanking.network.RestService;
import com.example.omgimbot.mapsbanking.ui.AnimationHelper;
import com.example.omgimbot.mapsbanking.ui.BottomDialogs;
import com.example.omgimbot.mapsbanking.ui.CustomDrawable;
import com.example.omgimbot.mapsbanking.ui.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {
    GoogleMap mMaps;
    @BindView(R.id.maps)
    MapView mapView;
    @BindView(R.id.myloc_fab)
    FloatingActionButton mMylocationFab;
    @BindView(R.id.list_fab)
    FloatingActionButton mListFab;
    @BindView(R.id.list_layout)
    LinearLayout mListMainLayout;
    @BindView(R.id.list_viewpager)
    ViewPager mListViewPager;
    NetworkService networkService;
    List<ApotekModel> mListMarker = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();
    Marker marker, myLocationMarker;
    private LatLng myLocation = new LatLng(0, 0);
    Animation slideUp, slideDown;
    List<FragmentMapsList> mTrackerFragments = new ArrayList<>();
    MenuItem searchItem;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        networkService = RestService.getRetroftInstance().create(NetworkService.class);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        mMylocationFab.setImageDrawable(CustomDrawable.googleMaterialDrawable(
                this, R.color.colorPrimaryDark, 24, GoogleMaterial.Icon.gmd_gps_fixed
        ));
        mMylocationFab.setOnClickListener(view -> zommToMyLocation());

        Bundle bundle = getIntent().getExtras();
        String apotek = bundle.getString("apotek");
        String map = bundle.getString("map");

        if (apotek != null){
            showTrackerList();
        } else if ( map.equals("maps")){
                hideTrackerList();
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setVisibility(View.VISIBLE);
        searchItem.setVisible(true);
        return true;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMaps = googleMap;
        mMaps.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMaps.setMyLocationEnabled(true);
        mMaps.getUiSettings().setMyLocationButtonEnabled(false);
        mMaps.setOnMyLocationChangeListener(location -> {
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                myLocation = lng;
                if (myLocationMarker == null) {
                    initMyLocationMarker();
                    getListApotek();
                } else myLocationMarker.setPosition(myLocation);
            }
        });
//        LatLng sydney = new LatLng(-34, 151);
//        mMaps.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMaps.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void initMyLocationMarker() {
        //IconFactory iconFactory = IconFactory.getInstance(this);
        Drawable d = CustomDrawable.googleMaterialDrawable(
                this, R.color.colorPrimaryDark, 24, GoogleMaterial.Icon.gmd_person_pin_circle);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.drawableToBitmap(d));
        if (mMaps != null) {
            myLocationMarker = mMaps.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("Lokasi Saya")
                    .icon(icon));

        }


    }

    public void zommToMyLocation() {
        mMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(
                myLocation, 18));
        myLocationMarker.showInfoWindow();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.mapView != null) {
            this.mapView.onStart();
        }
    }

    public void getListApotek() {

        Call<List<ApotekModel>> call = networkService.getListBank();
        call.enqueue(new Callback<List<ApotekModel>>() {
            @Override
            public void onResponse(Call<List<ApotekModel>> call, Response<List<ApotekModel>> response) {
//                progressDoalog.dismiss();
                if (response.isSuccessful()){
                    mListMarker = response.body();
                generateDataList(mListMarker);
                Log.d("Data :", "" + response.body().get(0).getId());
                }
            }

            @Override
            public void onFailure(Call<List<ApotekModel>> call, Throwable t) {
                //progressDoalog.dismiss();
                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getMessage", t.getMessage());
                Log.d("Localimessage", t.getLocalizedMessage());

            }
        });
    }

    private void generateDataList(List<ApotekModel> bankList) {
        mListMarker = bankList;
        int height = 120;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.apotek_icon);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        Log.d("Apotek List", String.valueOf(bankList.size()));
        Log.d("mListMarker ", String.valueOf(mListMarker.size()));

        if (mTitles.size() == 0 && myLocation != null) {
            for (int i = 0; i < mListMarker.size(); i++) {
                mTitles.add(mListMarker.get(i).getNama().split("\\|")[0]);
                LatLng location = new LatLng(Double.parseDouble(mListMarker.get(i).getLati()), Double.parseDouble(mListMarker.get(i).getLongi()));
                marker = mMaps.addMarker(new MarkerOptions().position(location).title(mListMarker.get(i).getId()).snippet(mListMarker.get(i).getDeskripsi())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                CameraPosition cmr = CameraPosition.builder().target(new LatLng(location.latitude, location.longitude)).zoom(12).bearing(0).tilt(45).build();
                mMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 11.0f));
                mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMaps.moveCamera(CameraUpdateFactory.newCameraPosition(cmr));
            }
            HashSet<String> hashSet = new HashSet<>();
            hashSet.addAll(mTitles);
            mTitles.clear();
            mTitles.addAll(hashSet);
            initTrackerList();
        } else {
            if (myLocation != null) {
                updateTrackerList();
            }
        }


    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        String cityName = "";
        marker.hideInfoWindow();
        ApotekModel apotekModel = null;
        int color = 0;
        for (ApotekModel t : mListMarker) {
            if (t.getId().equals(marker.getTitle())) {
                apotekModel = t;
            }
        }

        if (apotekModel != null) {
            BottomDialogs.showTrackerDetail(this,
                    getSupportActionBar().getTitle().toString(),
                    apotekModel, myLocation);

        }
        return true;
    }

    public void initTrackerList() {
        mListFab.setOnClickListener(this);
        final MapsPageAdapter adapter = new MapsPageAdapter(getSupportFragmentManager());
        for (int i = 0; i < mTitles.size(); i++) {
            FragmentMapsList fragmentTracker = new FragmentMapsList();
            List<ApotekModel> models = new ArrayList<>();
            for (int j = 0; j < mListMarker.size(); j++) {
                String trackerName = mListMarker.get(j).getNama().split("\\|")[0];
                models.add(mListMarker.get(j));
            }
            fragmentTracker.setData(models, myLocation);
            adapter.addFragment(fragmentTracker, mTitles.get(i));
            mTrackerFragments.add(fragmentTracker);
        }
        mListViewPager.setAdapter(adapter);


    }

    public void updateTrackerList() {
        for (int i = 0; i < mTitles.size(); i++) {
            List<ApotekModel> models = new ArrayList<>();
            for (int j = 0; j < mListMarker.size(); j++) {
                String trackerName = mListMarker.get(j).getNama().split("\\|")[0];
                models.add(mListMarker.get(j));

            }
            mTrackerFragments.get(i).setData(models, myLocation);
        }
    }


    public void showTrackerList() {
        if (slideUp == null) {
            slideUp = AnimationHelper.getAnimation(this, R.anim.slide_up, anim -> {
                mMylocationFab.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                searchItem.setVisible(true);

            });
        }
        mListFab.setImageDrawable(CustomDrawable.googleMaterialDrawable(
                this, R.color.colorPrimary, 24, GoogleMaterial.Icon.gmd_keyboard_arrow_down
        ));
        mListMainLayout.setVisibility(View.VISIBLE);
        mListMainLayout.startAnimation(slideUp);
    }


    public void hideTrackerList() {
        if (slideDown == null) {
            slideDown = AnimationHelper.getAnimation(this, R.anim.slide_down, anim -> {
                mListMainLayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                searchItem.setVisible(false);
                mListFab.setImageDrawable(CustomDrawable.googleMaterialDrawable(
                        this, R.color.colorPrimary, 24, GoogleMaterial.Icon.gmd_keyboard_arrow_up
                ));
                mMylocationFab.setVisibility(View.VISIBLE);
            });
        }
        mListMainLayout.startAnimation(slideDown);
    }

    public void zoomToLng(String deviceID) {
        LatLng dest = new LatLng(0, 0);
        for (int i = 0; i < mListMarker.size(); i++) {
            if (mListMarker.get(i).getId().equals(deviceID)) {
                LatLng lng = new LatLng(
                        Double.parseDouble(mListMarker.get(i).getLati()),
                        Double.parseDouble(mListMarker.get(i).getLongi())
                );
                dest = lng;
                mMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        lng, 18));
            }

        }
        String directionApiPath = Helper.getUrl(String.valueOf(myLocation.latitude), String.valueOf(myLocation.longitude),
                String.valueOf(dest.latitude), String.valueOf(dest.longitude));
        getDirectionFromDirectionApiServer(directionApiPath);

        Log.d("origin", ""+myLocation);
        Log.d("destination", ""+dest);
//        DrawRouteMaps.getInstance(this)
//                .draw(dest, myLocation, mMaps);
//        DrawMarker.getInstance(this).draw(mMaps, myLocation, R.drawable.ic_close_dark, "Origin Location");
//        DrawMarker.getInstance(this).draw(mMaps, dest, R.drawable.ic_close_light, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myLocation)
                .include(dest).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

    }

    private void getDirectionFromDirectionApiServer(String url) {
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(MapsActivity.this).addToRequestQueue(serverRequest);
    }

    private com.android.volley.Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new com.android.volley.Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    Log.d("JSON Response", response.toString());
                    if (response.getStatus().equals("OK")) {
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(mMaps, mDirections);
                    } else {
                        Toast.makeText(MapsActivity.this, "error connect to api direction !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };
    }

    private List<LatLng> getDirectionPolylines(List<RouteObject> routes) {
        List<LatLng> directionList = new ArrayList<LatLng>();
        for (RouteObject route : routes) {
            List<LegsObject> legs = route.getLegs();
            for (LegsObject leg : legs) {
                List<StepsObject> steps = leg.getSteps();
                for (StepsObject step : steps) {
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline) {
                        directionList.add(direction);
                    }

                }
            }
        }
        return directionList;
    }


    private com.android.volley.Response.ErrorListener createRequestErrorListener() {
        return new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (this.mapView != null) {
            this.mapView.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (this.mapView != null) {
            this.mapView.onPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (this.mapView != null) {
            this.mapView.onResume();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mapView != null) {
            this.mapView.onDestroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.zoomall_fab:
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                for (DataBankModel tracker : trackers) {
//                    LatLng lng = new LatLng(tracker.getLocation().getCoordinates().get(1),
//                            tracker.getLocation().getCoordinates().get(0));
//                    builder.include(lng);
//                    LatLngBounds bounds = builder.build();
//                    Gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 180));
//                    //Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(lng, 17));
//                }
//                break;
            case R.id.myloc_fab:
                if (myLocation.latitude != 0 && myLocation.longitude != 0)
                    this.zommToMyLocation();
                break;
            case R.id.list_fab:
                if (mListMainLayout.getVisibility() == View.VISIBLE)
                    this.hideTrackerList();
                else this.showTrackerList();
                break;
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(MapsActivity.this, home.class);
        startActivity(i);
        finish();
    }

}
