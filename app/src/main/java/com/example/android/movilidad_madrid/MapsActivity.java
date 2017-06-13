package com.example.android.movilidad_madrid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

//import org.neo4j.rest.graphdb.RestAPI;
//import org.neo4j.rest.graphdb.RestAPIFacade;
//import org.neo4j.rest.graphdb.query.QueryEngine;
//import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
//import org.neo4j.rest.graphdb.util.QueryResult;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    private String Destino_Neo = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
//
//        Log.d("a", ""+hasLoggedIn);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("hasLoggedIn", false);
//        editor.commit();

        if (hasLoggedIn){
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();

            builder.setLatLngBounds(new LatLngBounds(new LatLng(42.0, -3.0), new LatLng(45.0, -5.0)));
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
            startActivity(new Intent(MapsActivity.this, LoginActivity.class));
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions.length == 1 && permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

//        LatLng madrid = new LatLng(40.416733, -3.703313);
//        mMap.addMarker(new MarkerOptions().position(madrid).title("Madrid"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));
//        CameraUpdateFactory.newLatLngZoom(new LatLng(40.416733, -3.703313), 5);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.416733, -3.703313), 10));
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Driver driver = GraphDatabase.driver("bolt://hobby-ebblnhmhoeaggbkepcpfeipl.dbs.graphenedb.com:24786", AuthTokens.basic("root", "b.kw8DoVqt3lgi.wqQn17S5BBkUwGXA"));

//        RestAPI graphDb = new RestAPIFacade("http://10.0.2.2:7474/db/data");
//        QueryEngine engine =new RestCypherQueryEngine(graphDb);
//        Session session = driver.session();
//        engine.query("create (p:Person {name:\"Steve\"}) return p", Collections.EMPTY_MAP);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title((String) place.getName()));
                Log.d("a", place.getLatLng().latitude + "," + place.getLatLng().longitude );
                new JsonTask().execute("http://192.168.56.1:3000/?tagId=" + place.getLatLng().latitude + "," + place.getLatLng().longitude);
            }
        }

//        session.close();
//        driver.close();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MapsActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            Destino_Neo = result;

            String[] coordenadas = Destino_Neo.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
            float latitud = Float.parseFloat(coordenadas[0]);
            float longitud = Float.parseFloat(coordenadas[1]);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitud, longitud))
                    .title("Destino para aparcar"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 17));
        }
    }
}
