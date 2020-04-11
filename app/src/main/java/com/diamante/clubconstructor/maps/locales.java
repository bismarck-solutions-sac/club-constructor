package com.diamante.clubconstructor.maps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.LocalListAdapter;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.functions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class locales extends AppCompatActivity implements OnMapReadyCallback {

    private functions function = functions.getInstance();

    private AlertDialog dialog;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocalListAdapter localListAdapter;
    private static int REQUEST_GPS_ENABLED = 1;
    private static int REQUEST_GPS_PERMISSION = 1;
    private List<Local> local;

    private RecyclerView recyclerView;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        if (locationManager != null) {
            locationManager.removeUpdates(locationListenerGPS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        if (locationManager != null) {
            locationManager.removeUpdates(locationListenerGPS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locales);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

        try {
            context = this;
            recyclerView = findViewById(R.id.recycler);
            toolbar = findViewById(R.id.toolbar);
            dialog = createDialogProgress();

            setSupportActionBar(toolbar);
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setTitle("Ubícanos");
                }
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            if (isLocationEnabled()) {
                setup_googleMap();
                loadData();
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onCreate").show();
        }
    }

    private void setup_googleMap() {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onMapReady").show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)      // Sets the center of the map to User Location View
                        .zoom(5)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onMapReady").show();
        }
    }

    public void addMarker(float longitude, float latitude, String title) {
        try {
            LatLng ubicacion = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(ubicacion).draggable(false).title(title));
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: addMarker").show();
        }
    }

    private void loadData() {
        dialog.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getLocalList();
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            local = response.body().local;
                            for (int i = 0; i < local.size(); i++) {
                                addMarker(local.get(i).longitude, local.get(i).latitude, local.get(i).name);
                            }
                            localListAdapter = new LocalListAdapter(context, local, null);
                            recyclerView.setAdapter(localListAdapter);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                            }
                            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location!=null){
                                calcular_distancia(location);
                            }
                            dialog.dismiss();
                        } else if (request.getCodigoError() == 2) {
                            dialog.dismiss();
                            createDialogError(request.getMensajeSistema(), "Validación").show();
                        }
                    } else {
                        dialog.dismiss();
                        createDialogError("Se produjo un error no identificado", "Error: loadData").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    createDialogError(t.getMessage(), "Validación").show();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            createDialogError(e.getMessage(), "Error: loadData").show();
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng ubicacion;
            try {
                if (location.getLatitude() != 0 || location.getLongitude() != 0) {
                    ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    location    = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    ubicacion   = new LatLng(location.getLatitude(), location.getLongitude());
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)      // Sets the center of the map to User Location View
                        .zoom(10)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                calcular_distancia(location);

            } catch (Exception e) {
                createDialogError(e.getMessage(), "Error: onLocationChanged").show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void calcular_distancia(Location location){
        LatLng  latLng  = null;
        double  distance;
        try {
            if (local != null && location != null) {
                LatLng StartP = new LatLng(location.getLongitude(), location.getLatitude());
                for (int i = 0; i < local.size(); i++) {
                    LatLng EndP = new LatLng(local.get(i).longitude, local.get(i).latitude);
                    distance = function.CalculationByDistance(StartP, EndP);
                    local.get(i).setDistance(distance);
                }
                Collections.sort(local, new Comparator<Local>() {
                    @Override
                    public int compare(Local o1, Local o2) {
                        return new Double(o1.getDistance()).compareTo(new Double(o2.getDistance()));
                    }
                });
                latLng = new LatLng(location.getLongitude(), location.getLatitude());
            }
            localListAdapter = new LocalListAdapter(context, local, latLng);
            recyclerView.setAdapter(localListAdapter);
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: calcular_distancia").show();
        }
    }

    private boolean isLocationEnabled() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_GPS_ENABLED);
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(locales.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION);
                } else {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 10, locationListenerGPS);
                    return true;
                }

            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: isLocationEnabled").show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GPS_ENABLED) {
            if (isLocationEnabled()) {
                setup_googleMap();
                loadData();
            }
        } else if (requestCode == REQUEST_GPS_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            setup_googleMap();
            loadData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case 1 /* El codigo que puse a mi request */: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 10, locationListenerGPS);
                        setup_googleMap();
                        loadData();
                    } else {
                        if (isLocationEnabled()) {
                            setup_googleMap();
                            loadData();
                        }
                    }
                    return;
                }
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onRequestPermissionsResult").show();
        }
    }

    public AlertDialog createDialogProgress() {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            try {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_circularloader, null);
                builder.setView(v);
                alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AlertDialog createDialogError(String message, String title) {
        try {
            final Button btn_ok;
            final TextView textTitle, textMessage;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_error, null);

            btn_ok = v.findViewById(R.id.buttonAction);
            textTitle = v.findViewById(R.id.textTitle);
            textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(title);
            textMessage.setText(message);
            btn_ok.setText(getResources().getString(R.string.okay));
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
