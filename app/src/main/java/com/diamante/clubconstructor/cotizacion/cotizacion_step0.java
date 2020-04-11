package com.diamante.clubconstructor.cotizacion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.adapter.LocalListAdapter;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_step0 extends AppCompatActivity implements OnMapReadyCallback {

    private functions function = functions.getInstance();

    private StepperIndicator stepperIndicator;
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView textdireccion;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocalListAdapter localListAdapter;
    private static int REQUEST_GPS_ENABLED = 1;
    private static int REQUEST_GPS_PERMISSION = 1;
    private List<Local> local;
    private Estimated estimated;
    private AlertDialog dialog;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            local = (List<Local>) savedInstanceState.getSerializable("Local");
            estimated = (Estimated) savedInstanceState.getSerializable("estimated");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("local", (Serializable) local);
            state.putSerializable("estimated", estimated);

        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }

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
        setContentView(R.layout.activity_cotizacion_step0);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("local") != null){
                local = (List<Local>) savedInstanceState.getSerializable("local");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context = this;
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                estimated = (Estimated) bundle.getSerializable("estimated");
            }
            dialog = createDialogProgress();
            stepperIndicator = findViewById(R.id.stepper_indicator);
            stepperIndicator.setStepCount(constantes.STEP_COTIZACION_COUNT);
            stepperIndicator.setCurrentStep(0);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            recyclerView    = findViewById(R.id.recycler);
            textdireccion   = findViewById(R.id.textdireccion);
            ImageView home  = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
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
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private boolean isLocationEnabled() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_GPS_ENABLED);
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(cotizacion_step0.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION);
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

    private void mostrarLocalizacion(Location location) {
        if (location!=null){
            if (location.getLatitude() != 0.0 && location.getLatitude() != 0.0) {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!list.isEmpty()) {
                        Address address = list.get(0);
                        textdireccion.setText(address.getAddressLine(0));
                    }
                } catch (Exception e) {
                    createDialogError(e.getMessage(), "Error: mostrarLocalizacion").show();
                }
            }
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
                        .zoom(15)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mostrarLocalizacion(location);
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onMapReady").show();
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
                mostrarLocalizacion(location);

                addMarker(ubicacion.latitude, ubicacion.longitude, textdireccion.getText().toString());

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)      // Sets the center of the map to User Location View
                        .zoom(15)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
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
        double  distance;
        try {
            if (local != null && location!=null) {
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
                int count = 0, rows;
                rows = local.size();
                for (int i = 0; i < rows; i++) {
                    count++;
                    if (count > 999) {
                        local.remove(i);
                        rows--;
                        i--;
                    }
                    if (textdireccion.getText().length()!=0){
                        local.get(i).setAddress_2(textdireccion.getText().toString());
                    }
                }
            }
            localListAdapter = new LocalListAdapter(context, local, estimated);
            recyclerView.setAdapter(localListAdapter);
            localListAdapter.notifyDataSetChanged();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: calcular_distancia").show();
        }
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                            }
                            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                            localListAdapter = new LocalListAdapter(context, local, estimated);
                            recyclerView.setAdapter(localListAdapter);
                            localListAdapter.notifyDataSetChanged();
                            calcular_distancia(location);
                            dialog.dismiss();
                        } else if (request.getCodigoError() == 2) {
                            dialog.dismiss();
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                        }
                    } else {
                        dialog.dismiss();
                        createDialogError("Se produjo un error no identificado", "Error: loadData").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    createDialogError(t.getMessage(), "Error: Validación").show();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            createDialogError(e.getMessage(), "Error: loadData").show();
        }
    }

    public void addMarker(double longitude, double latitude, String title) {
        try {
            LatLng ubicacion = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(ubicacion).draggable(false).title(title));
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: addMarker").show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog createDialogError(String message, String title) {
        try {
            final Button btn_ok;
            final TextView textTitle, textMessage;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_error, null);

            btn_ok      = v.findViewById(R.id.buttonAction);
            textTitle   = v.findViewById(R.id.textTitle);
            textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(title);
            textMessage.setText(message);
            btn_ok.setText(context.getResources().getString(R.string.okay));
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

    public AlertDialog createDialogProgress() {
        try {
            AlertDialog alertDialog =null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            try {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_circularloader, null);
                builder.setView(v);
                alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }catch (Exception e){
                e.printStackTrace();
            }
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
