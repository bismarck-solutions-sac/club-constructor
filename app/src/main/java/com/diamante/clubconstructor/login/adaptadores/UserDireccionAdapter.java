package com.diamante.clubconstructor.login.adaptadores;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.SpinnerSingleAdapter;
import com.diamante.clubconstructor.dialogos.dialog_single;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.Direccion;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.UserEmpresa;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.MaestrosRequest;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDireccionAdapter extends RecyclerView.Adapter<UserDireccionAdapter.ViewHolder>{

    private functions function = functions.getInstance();
    private Context context;
    private List<Direccion> direccions;
    private Cotizacion cotizacion;
    public int lastCheckedPosition =-1;

    private EditText textdato, edt_ref, edt_dpto, edt_prov, edt_dist;
    private String SELECTOR;

    public UserDireccionAdapter(Context context, List<Direccion> direccions, Cotizacion cotizacion, int position){
        this.context                = context;
        this.direccions             = direccions;
        this.cotizacion             = cotizacion;
        this.lastCheckedPosition    = position;
    }

    public UserDireccionAdapter(Context context, List<Direccion> direccions, Cotizacion cotizacion){
        this.context    = context;
        this.direccions = direccions;
        this.cotizacion = cotizacion;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = null;
        ViewHolder viewHolder =null;
        try {
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_direccion_single, parent, false);
            viewHolder = new ViewHolder(ItemView);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{

            holder.textDireccion.setText(direccions.get(position).getDireccion());
            holder.textReferencia.setText(direccions.get(position).getReferencia());
            holder.selected.setChecked(position == lastCheckedPosition);
            if (lastCheckedPosition == position){
                holder.selected.setChecked(true);
                direccions.get(position).setSelected(true);
            }else{
                holder.selected.setChecked(false);
                direccions.get(position).setSelected(false);
            }
            holder.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCheckedPosition = position;
                    notifyDataSetChanged();
                }
            });
            holder.imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    direccions.get(position).setAccion("Update");
                    createDialogLugar(direccions.get(position), position).show();
                }
            });
            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    direccions.get(position).setAccion("Delete");
                    createDialogLugar(direccions.get(position), position).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateCircularReveal(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (direccions!=null){
            return direccions.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textDireccion, textReferencia;
        private RadioButton selected;
        private ImageView imgedit, imgdelete;
        public ViewHolder(View Item){
            super(Item);
            try {
                textDireccion   = Item.findViewById(R.id.textDireccion);
                textReferencia  = Item.findViewById(R.id.textReferencia);
                selected        = Item.findViewById(R.id.radioSelect);
                imgedit         = Item.findViewById(R.id.imgedit);
                imgdelete       = Item.findViewById(R.id.imgdelete);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(),view.getHeight());
        Animator animator = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
        }
        view.setVisibility(View.VISIBLE);
        animator.start();
    }

    public List<Direccion> getDireccions(){
        try {
            return this.direccions;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Direccion select(){
        try {
            if (direccions!=null){
                return direccions.get(lastCheckedPosition);
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AlertDialog createDialogLugar( Direccion userDireccion, int position) {
        final Button btn_ok, btn_no;
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_lugarentrega, null);

        TextView text_title;

        btn_ok          = v.findViewById(R.id.btn_ok);
        btn_no          = v.findViewById(R.id.btn_cancel);
        textdato        = v.findViewById(R.id.edt_direccion);
        edt_ref         = v.findViewById(R.id.edt_referencia);
        edt_dpto        = v.findViewById(R.id.edt_departamento);
        edt_prov        = v.findViewById(R.id.edt_provincia);
        edt_dist        = v.findViewById(R.id.edt_distrito);
        text_title      = v.findViewById(R.id.textTitle);

        textdato.setText(userDireccion.getDireccion());
        edt_ref.setText(userDireccion.getReferencia());
        edt_dpto.setText(userDireccion.getDepartamento_name());
        edt_prov.setText(userDireccion.getProvincia_name());
        edt_dist.setText(userDireccion.getCodigopostal_name());
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        text_title.setText("Agregar dirección de despacho" );
        if (userDireccion.getAccion().equals("Delete")){
            text_title.setText("Eliminar dirección de despacho" );
            textdato.setEnabled(false);
            edt_ref.setEnabled(false);
            edt_dpto.setEnabled(false);
            edt_prov.setEnabled(false);
            edt_dist.setEnabled(false);
        }else if (userDireccion.getAccion().equals("Update")){
            text_title.setText("Editar dirección de despacho" );
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userDireccion.getAccion().equals("Delete")) {
                    userDireccion.setDireccion(textdato.getText().toString());
                    userDireccion.setReferencia(edt_ref.getText().toString());
                    if (userDireccion.getDepartamento() == null) {
                        edt_dpto.setError("El departamento es requerido");
                        return;
                    }
                    if (userDireccion.getDepartamento().length() == 0) {
                        edt_dpto.setError("El departamento es requerido");
                        return;
                    }
                    if (userDireccion.getProvincia() == null) {
                        edt_prov.setError("La provincia es requerida");
                        return;
                    }
                    if (userDireccion.getProvincia().length() == 0) {
                        edt_prov.setError("La provincia es requerida");
                        return;
                    }
                    if (userDireccion.getCodigopostal() == null) {
                        edt_dist.setError("El distrito es requerido");
                        return;
                    }
                    if (userDireccion.getCodigopostal().length() == 0) {
                        edt_dist.setError("El distrito es requerido");
                        return;
                    }
                }
                Update(userDireccion, position, alertDialog);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        edt_dpto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTOR = "DPTO";
                getDepartamento(userDireccion, position);
            }
        });
        edt_prov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTOR = "PROV";
                getProvincia(userDireccion, position);
            }
        });
        edt_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTOR = "DIST";
                getDistrito(userDireccion, position);
            }
        });
        return alertDialog;
    }

    private void Update(Direccion direccion, int position, AlertDialog alertDialog){
        try {
            if (direccion.getPersona()==0){
                direccions.remove(position);
                if (!direccion.getAccion().equals("Delete")){
                    direccions.add(position, direccion);
                }
                notifyDataSetChanged();
                alertDialog.dismiss();
                return;
            }
            if (direccion.getSecuencia()==0){
                direccion.setAccion("Add");
            }
            RequestParameter parameter = new RequestParameter();
            parameter.direccion = direccion;
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result = methodWS.direccionAdd (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            if (direccion.getAccion().equals("Delete")){
                                direccions.remove(position);
                            }else {
                                if (direccion.getAccion().equals("Add")){
                                    direccion.setSecuencia(request.direccion.getSecuencia());
                                }
                                direccions.remove(position);
                                direccions.add(position, direccion);
                            }
                            notifyDataSetChanged();
                            alertDialog.dismiss();
                        }else if(request.getCodigoError()==2){
                            Toast.makeText(context, request.mensajeSistema, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getDepartamento(Direccion userDireccion, int position){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("DEPARTAMENTOS");
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int LINEA=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            LINEA = function.read_generalspinner_position(request.spinner_general, userDireccion.getDepartamento());
                            createDialogSingle(request.spinner_general, LINEA, position).show();
                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), "Error: onResponse").show();
                        }
                    }else{
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: Update").show();
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

    public AlertDialog createDialogSingle(List<GeneralSpinner> spinner_general, int LINEA, int POSITION) {
        final SpinnerSingleAdapter spinnerSingleAdapter;
        final TextView textTitle;
        final Button btn_ok;
        final RecyclerView recyclerView;
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_single, null);

        btn_ok      = v.findViewById(R.id.buttonAction);
        textTitle   = v.findViewById(R.id.textTitle);
        recyclerView= v.findViewById(R.id.recycler);
        btn_ok.setText(context.getResources().getString(R.string.okay));
        textTitle.setText("Seleccione un valor");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        spinnerSingleAdapter = new SpinnerSingleAdapter(context, spinner_general);
        recyclerView.setAdapter(spinnerSingleAdapter);

        spinnerSingleAdapter.lastCheckedPosition = LINEA;

        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (SELECTOR){
                        case "DPTO":
                            direccions.get(POSITION).setDepartamento(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getId());
                            direccions.get(POSITION).setDepartamento_name(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            direccions.get(POSITION).setProvincia("");
                            direccions.get(POSITION).setCodigopostal("");
                            edt_dpto.setError(null);
                            edt_dpto.setText(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            edt_prov.setText("");
                            edt_prov.setError(null);
                            edt_dist.setText("");
                            edt_dist.setError(null);
                            break;
                        case "PROV":
                            direccions.get(POSITION).setProvincia(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getId());
                            direccions.get(POSITION).setProvincia_name(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            direccions.get(POSITION).setCodigopostal("");
                            edt_prov.setText(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            edt_prov.setError(null);
                            edt_dist.setText("");
                            edt_dist.setError(null);
                            break;
                        case "DIST":
                            direccions.get(POSITION).setCodigopostal(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getId());
                            direccions.get(POSITION).setCodigopostal_name(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            edt_dist.setText(spinner_general.get(spinnerSingleAdapter.lastCheckedPosition).getDescripcion());
                            edt_dist.setError(null);
                            break;
                    }

                }catch (Exception e){
                    createDialogError(e.getMessage(), "Error: onDialogPositiveClick").show();
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    private void getProvincia(Direccion userDireccion, int position){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("PROVINCIAS");
        dato.setParametro1(userDireccion.getDepartamento());
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int LINEA=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            LINEA = function.read_generalspinner_position(request.spinner_general, userDireccion.getProvincia());
                            createDialogSingle(request.spinner_general, LINEA, position).show();
                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), "Error: onResponse").show();
                        }
                    }else{
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private void getDistrito(Direccion userDireccion, int position){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("DISTRITOS");
        dato.setParametro1(userDireccion.getDepartamento());
        dato.setParametro2(userDireccion.getProvincia());
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int LINEA=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            LINEA = function.read_generalspinner_position(request.spinner_general, userDireccion.getCodigopostal());
                            createDialogSingle(request.spinner_general, LINEA, position).show();
                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), "Error: onResponse").show();
                        }
                    }else{
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }
}