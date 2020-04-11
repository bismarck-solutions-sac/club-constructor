package com.diamante.clubconstructor.maps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class locales_detail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView textName, textAddress, textDistance, textResponsable, textPhone;
    private Bundle bundle;
    private Local local;
    private Toolbar toolbar;
    NumberFormat distance   = new DecimalFormat("#,##0");
    private Button btn_llegar;
    private LatLng latLng;
    private Context context;

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locales_detail);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        inicializa();
    }

    private void inicializa() {
        try{
            context         = this;
            bundle          = this.getIntent().getExtras();
            textName        = findViewById(R.id.name);
            textAddress     = findViewById(R.id.address);
            textDistance    = findViewById(R.id.distance);
            textResponsable = findViewById(R.id.responsable);
            textPhone       = findViewById(R.id.phone);
            local           = (Local) bundle.getSerializable("Local");
            latLng          = (LatLng) bundle.get("latLng");
            btn_llegar      = findViewById(R.id.btn_llegar);
            toolbar         = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setTitle("Ubícanos");
                }
            }
            if (local!=null){
                textName.setText(local.getName());
                textAddress.setText(local.getAddress());
                textDistance.setText(distance.format(local.distance)+ "\nkm");
                textResponsable.setText(local.getAdmin());
                textPhone.setText(local.free_phone);
            }
            btn_llegar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=My%20Location&daddr="+local.latitude+","+local.longitude));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER );
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    try {
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finish();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng ubicacion;
        try {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (local!=null){
                ubicacion = new LatLng(local.latitude, local.longitude);
                mMap.addMarker(new MarkerOptions().position(ubicacion).draggable(false).title(local.name));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(ubicacion)      // Sets the center of the map to User Location View
                        .zoom(5)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.getUiSettings().setMyLocationButtonEnabled(true );
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onMapReady").show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }catch (Exception e){
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
