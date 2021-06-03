package com.example.moviesapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class CinemaFinderActivity extends AppCompatActivity {
    Spinner spType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;
    LatLng currentLatLng = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_finder);

        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        String[] placeTypeList = {"movie_theater"};
        String[] placeNameList = {"Cinema"};

        spType.setAdapter(new ArrayAdapter<>(CinemaFinderActivity.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Checking Permissions
        if(ActivityCompat.checkSelfPermission(CinemaFinderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //If permission is granted
            getCurrentLocation();
        } else {
            //If permission is denied
            ActivityCompat.requestPermissions(CinemaFinderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get selected position of spinner
                int i = spType.getSelectedItemPosition();
                //Initialize URL
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + //url
                "?location=" + currentLat + "," + currentLong + //location latitude and longitude
                "&radius=15000" + //nearby radius
                "&types=" + placeTypeList[i] + //place type
//                "&sensor=true" + // sensor
                "&key=" + getResources().getString(R.string.google_map_key); //Maps Key

                //Executing place task method to download the json data
                new PlaceTask().execute(url);
            }
        });
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                    currentLatLng = new LatLng(currentLat, currentLong);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map = googleMap;
                            map.addMarker(new MarkerOptions()
                                .position(currentLatLng)
                                .title("You are here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat, currentLong), 13));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try { //Initializing data
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Execute parser task
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //Initializing url
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        //String builder
        StringBuilder builder = new StringBuilder();
        //String variable
        String line = "";
        while((line = reader.readLine()) != null){
            //Append line
            builder.append(line);
        }
        //Get appended data
        String data = builder.toString();
        //Closing reader and returning data
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Creating json parser class
            ParseJson parseJson = new ParseJson();
            //Initializing hashmap list
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                //Initializing json object
                object = new JSONObject(strings[0]);
                //Parsing json object
                mapList = parseJson.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Returning the map list
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //Clearing map
            Toast.makeText(CinemaFinderActivity.this, "Looking for places..", Toast.LENGTH_SHORT).show();
            map.clear();
            for(int i = 0; i < hashMaps.size(); i++){
                //Initializing hash map
                HashMap<String, String> hashMapList = hashMaps.get(i);
                //Getting latitude
                double lat = Double.parseDouble(hashMapList.get("lat"));
                //Getting longitude
                double lng = Double.parseDouble(hashMapList.get("lng"));
                //Getting name
                String name = hashMapList.get("name");
                //Concatenating latitude and longitude
                LatLng latLng = new LatLng(lat, lng);
                //Initializing marker options
                MarkerOptions options = new MarkerOptions();
                //Setting position
                options.position(latLng);
                //Setting title
                options.title(name);
                //Adding the marker on the map
                map.addMarker(options);
            }
            map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }
}
