package com.example.rahultasker.brewme;



import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private LatLng latLng;
    private double lat1,lon1;
    private BrewRecord br;
    private String country;
    private SavedDatabase db;

    Geocoder geocoder = null;
    ArrayList<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mapper");
        actionBar.setDisplayHomeAsUpEnabled(true);
        geocoder = new Geocoder(this);
        locations = new ArrayList<String>();
        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();
        br = (BrewRecord) bundle.getSerializable("brewery");
        lat1 = Double.parseDouble(br.getLatitude());
        lon1 = Double.parseDouble(br.getLongitude());
        String msg = "Lng: " + br.getLongitude()+ " Lat: " + br.getLatitude();
        Log.v("in map",msg);
        System.out.println(msg);
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnMapLoadedCallback(this);
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
    }

    @Override
    public void onMapLoaded() {
        getMoreInfo(); // call this --> use a geoCoder to find the location of the eq
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
             public void onInfoWindowLongClick(Marker marker) {
                db = Room.databaseBuilder(getApplicationContext(), SavedDatabase.class, db.NAME).fallbackToDestructiveMigration().build();
                List<SavedBrewRecord> list = new ArrayList<>();
                SavedBrewRecord sbr = convertBrToSbr(br);
                list.add(sbr);
                new insertToSaved(db, SavedActivity.recyclerView, SavedActivity.adapter, getApplicationContext()).execute(list);
             }
         });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String final_url = br.getWebsite_url();
                Uri uri = Uri.parse(final_url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    public SavedBrewRecord convertBrToSbr(BrewRecord br) {
        SavedBrewRecord sbr = new SavedBrewRecord();
        sbr.setName(br.getName());
        sbr.setBrewery_type(br.getBrewery_type());
        sbr.setCity(br.getCity());
        sbr.setCountry(br.getCountry());
        sbr.setLatitude(br.getLatitude());
        sbr.setLongitude(br.getLongitude());
        sbr.setPhone(br.getPhone());
        sbr.setPostal_code(br.getPostal_code());
        sbr.setStreet(br.getStreet());
        sbr.setWebsite_url(br.getWebsite_url());
        sbr.setState(br.getState());
        sbr.setBrewId(br.getBrewId());
        return sbr;
    }

    public void getMoreInfo() {
        System.out.println("in getMoreInfo " + lat1 + " " + lon1);
        latLng = new LatLng(lat1, lon1);
        Geocoder gcd = new Geocoder(this);
        try {
            List<Address> list = gcd.getFromLocation(lat1, lon1, 10);
            if (list != null & list.size() > 0) {
                country = list.get(0).getCountryName();
                if (country==null)
                    country = "unknown country";
                System.out.println("in map getMoreInfo country " + country);
            }
            else { //no location found
                country = "unknown country";
                System.out.println("in getMoreInfo no location found");
            }
        } catch (IOException e) //no geo address found
        {
            country = "unknown country";
            Log.v("in map new test","hhhh");
        }
        // puts marker icon at location
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(String.valueOf(br.getName()))
                .snippet(br.getStreet() + ", " + br.getCity() + ", " +br.getState())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14), 3000, null);
    }
}
