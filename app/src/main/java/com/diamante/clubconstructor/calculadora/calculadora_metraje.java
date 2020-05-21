package com.diamante.clubconstructor.calculadora;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.principal;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class calculadora_metraje extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private TextView text_desc, text_title;
    private EditText edit_largo, edit_alto;
    private Button btnContinue;
    private static String EDIT_TAG = "L";
    private RecyclerView recyclerView;

    private globals global = globals.getInstance();
    private GeneralSpinner brick_type;
    private Estimated estimated;
    private List<EstimatedDetail> detail = new ArrayList<>();

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            global      = (globals) savedInstanceState.getSerializable("global");
            brick_type  = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            detail      = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            text_desc.setText(savedInstanceState.getString("descripcion"));
            text_title.setText(savedInstanceState.getString("title"));

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", global);
            state.putSerializable("brick_type", brick_type);
            state.putSerializable("estimated", estimated);
            state.putSerializable("detail", (Serializable) detail);
            state.putString("descripcion", text_desc.getText().toString());
            state.putString("title", text_title.getText().toString());
        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_back_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_back_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_metraje);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
            }
            if (savedInstanceState.getSerializable("brick_type") != null){
                brick_type = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
            if (savedInstanceState.getSerializable("detail") != null){
                detail = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            }
            if (savedInstanceState.getString("descripcion") != null){
                text_desc.setText(savedInstanceState.getString("descripcion"));
            }
            if (savedInstanceState.getString("title") != null){
                text_title.setText(savedInstanceState.getString("title"));
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle   = this.getIntent().getExtras();
            context     = this;
            toolbar     = findViewById(R.id.toolbar);
            text_desc   = findViewById(R.id.textdescripcion);
            text_title  = findViewById(R.id.textTitle);
            edit_largo  = findViewById(R.id.editLargo);
            edit_alto   = findViewById(R.id.editAlto);
            btnContinue = findViewById(R.id.btnContinue);
            recyclerView= findViewById(R.id.recycler);
            if (bundle!=null){
                brick_type  = (GeneralSpinner) bundle.getSerializable("brick_type");
                estimated   = (Estimated) bundle.getSerializable("estimated");
                if (estimated !=null){
                    detail      = estimated.getDetail();
                }
            }
            if (brick_type!= null){
                text_title.setText(brick_type.getDescripcion().toUpperCase());
                text_desc.setText(Html.fromHtml("Indíquenos los m" + "<sup>" + "2" + "</sup>" + " que necesitas para constuir " + brick_type.getDescripcion() +":"));
            }else{
                text_desc.setText(Html.fromHtml("Indíquenos los m" + "<sup>" + "2" + "</sup>" + " que necesitas para constuir" ));
            }
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.size()!=0 && global.user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                    }else{
                        Intent i = new Intent(getApplicationContext(), principal.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            edit_largo.requestFocus();
            edit_largo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_largo.setSelectAllOnFocus(true);
                    edit_largo.selectAll();
                    EDIT_TAG ="L";
                }
            });
            edit_largo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try{
                        if (charSequence.toString().trim().equals(".")){
                            return;
                        }
                        EDIT_TAG ="L";
                    }catch (Exception e){
                    }
                }
                @Override
                public void afterTextChanged(Editable et) {
                }
            });

            edit_alto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_alto.setSelectAllOnFocus(true);
                    edit_alto.selectAll();
                    EDIT_TAG ="A";
                }
            });
            edit_alto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try{
                        if (charSequence.toString().trim().equals(".")){
                            return;
                        }
                        EDIT_TAG ="A";
                    }catch (Exception e){
                    }
                }
                @Override
                public void afterTextChanged(Editable et) {
                }
            });
            edit_largo .setSelectAllOnFocus(true);
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (EDIT_TAG){
                        case "L":
                            edit_alto.setSelectAllOnFocus(true);
                            edit_alto.requestFocus();
                            EDIT_TAG ="A";
                            break;
                        case "A":
                            if (validate()){
                                Intent i = new Intent(context, calculadora_bricks.class);
                                i.putExtra("brick_type", brick_type);
                                i.putExtra("largo", edit_largo.getText().toString());
                                i.putExtra("alto", edit_alto.getText().toString());
                                i.putExtra("estimated", estimated);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                            break;
                    }
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (detail.size()!=0 && global.user.id>0){
                    createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                }else{
                    finish();
                }
            }
            return true;
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onKeyDown").show();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    if (detail.size()!=0 && global.user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                    }else{
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate(){
        try {
            if (edit_largo.getText().toString().length()==0 || edit_largo.getText().toString()==null){
                Toast.makeText(context, "Indique el largo de la estructura.", Toast.LENGTH_SHORT).show();
                edit_largo.requestFocus();
                EDIT_TAG="L";
                return false;
            }
            if (edit_alto.getText().toString().length()==0 || edit_alto.getText().toString()==null){
                Toast.makeText(context, "Indique el alto de la estructura.", Toast.LENGTH_SHORT).show();
                edit_alto.requestFocus();
                EDIT_TAG="A";
                return false;
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: validate").show();
        }
        return true;
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

    public AlertDialog createDialogQuestion(String message, String title) {
        final Button btn_ok, btn_no;
        final TextView textTitle, textMessage;
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_warning, null);

        btn_ok      = v.findViewById(R.id.buttonYes);
        btn_no      = v.findViewById(R.id.buttonNo);
        textTitle   = v.findViewById(R.id.textTitle);
        textMessage = v.findViewById(R.id.textMessage);

        textTitle.setText(title);
        textMessage.setText(message);
        btn_ok.setText(context.getResources().getString(R.string.okay));
        btn_no.setText(context.getResources().getString(R.string.cancelar));
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), principal.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                alertDialog.dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
}
