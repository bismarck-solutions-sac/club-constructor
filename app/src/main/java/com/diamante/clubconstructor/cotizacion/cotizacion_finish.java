package com.diamante.clubconstructor.cotizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.adapter.CotizacionCartAdapter;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.util.functions;

public class cotizacion_finish extends AppCompatActivity {

    private functions function = functions.getInstance();
    private Context context;
    private RecyclerView recyclerView;
    private TextView textNumero, textItems, textDireccion;
    private Cotizacion cotizacion;
    private CotizacionCartAdapter adapter;
    private AppCompatButton btnCotizar, btnList;
    private CardView cardview;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            cotizacion = (Cotizacion) savedInstanceState.getSerializable("cotizacion");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("cotizacion", cotizacion);

        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            function._ga("Cotización - Completa", context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion_finish);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("cotizacion") != null){
                cotizacion = (Cotizacion) savedInstanceState.getSerializable("cotizacion");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context         = this;
            textNumero      = findViewById(R.id.textNumero);
            textItems       = findViewById(R.id.textItems);
            textDireccion   = findViewById(R.id.textDireccion);
            recyclerView    = findViewById(R.id.recycler);
            btnCotizar      = findViewById(R.id.btnCotizar);
            btnList         = findViewById(R.id.btnList);
            cardview        = findViewById(R.id.cardview);
            Bundle bundle = this.getIntent().getExtras();
            cardview.setVisibility(View.GONE);
            if (bundle!=null){
                cotizacion  = (Cotizacion) bundle.getSerializable("Cotizacion");
                if (cotizacion!=null){
                    textNumero.setText("Cotización N°: " + cotizacion.numerodocumento);
                    textItems.setText(String.valueOf(cotizacion.detalle.size()) + " item(s)");
                    textDireccion.setText(cotizacion.lugarentrega);
                    if (cotizacion.recojoflag.equals("O")){
                        cardview.setVisibility(View.VISIBLE);
                    }
                }
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //recyclerView.addItemDecoration( new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new CotizacionCartAdapter(context, cotizacion);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            btnCotizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, cotizacion_ubicacion.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });

            btnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, cotizacion_list.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    public AlertDialog createDialogError(String message, String title) {
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
    }
}
