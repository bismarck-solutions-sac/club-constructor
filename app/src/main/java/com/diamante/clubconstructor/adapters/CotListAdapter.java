package com.diamante.clubconstructor.adapters;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CotListAdapter extends RecyclerView.Adapter<CotListAdapter.ViewHolder>{
    private Context context;
    private List<Cotizacion> detail;
    NumberFormat enteroFormat  = new DecimalFormat("#,##0");
    NumberFormat decimalFormat  = new DecimalFormat("#,##0.00");
    private AlertDialog dialog;
    public CotListAdapter(Context context, List<Cotizacion> detail){
        this.context= context;
        this.detail = detail;
        dialog      = createDialogProgress();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cotizacion_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(ItemView);
            return viewHolder;
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onCreateViewHolder").show();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            holder.textOrden.setText(detail.get(position).getNumerodocumento());
            holder.textItems.setText(enteroFormat.format(detail.get(position).getItems()));
            holder.textTotal.setText(decimalFormat.format(detail.get(position).getMontototal()));
            switch (detail.get(position).estado){
                case "PR":
                    holder.textEstado.setText("En Preparación");
                    break;
                case "AN":
                    holder.textEstado.setText("Anulado");
                    break;
                case "AP":
                    holder.textEstado.setText("Aprobado");
                    break;
                case "CO":
                    holder.textEstado.setText("Completado");
                    break;
            }
            holder.textFecha.setText(detail.get(position).getFechadocumento().substring(0, 10));
            holder.switchCompat.setChecked(false);
            if (detail.get(position).getRecojoflag().equals("O")){
                holder.switchCompat.setChecked(true);
            }
            holder.btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreatePDF(detail.get(position));
                }
            });

        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onBindViewHolder").show();
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
        if (detail!=null){
            return detail.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textOrden, textItems, textTotal, textEstado, textFecha;
        AppCompatButton btnVer;
        SwitchCompat switchCompat;
        public ViewHolder(View Item){
            super(Item);
            try {
                textOrden   = Item.findViewById(R.id.textNumero);
                textItems   = Item.findViewById(R.id.textItems);
                textTotal   = Item.findViewById(R.id.textTotal);
                textEstado  = Item.findViewById(R.id.textEstado);
                textFecha   = Item.findViewById(R.id.textFecha);
                btnVer      = Item.findViewById(R.id.btnVer);
                switchCompat= Item.findViewById(R.id.switch1);
            }catch (Exception e){
                createDialogError(e.getMessage(), "Error: ViewHolder").show();
            }
        }
    }

    public void animateCircularReveal(View view){
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(),view.getHeight());
        Animator animator = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animator = ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
            }
            view.setVisibility(View.VISIBLE);
            animator.start();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: animateCircularReveal").show();
        }
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

        }
        return null;
    }

    private void CreatePDF(Cotizacion cotizacion) {
        RequestParameter parameter = new RequestParameter();
        parameter.cotizacion = cotizacion;
        dialog.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.cotizacion_pdf(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            Uri uri = Uri.parse(constantes.URL_BASE_IMAGE + "PDF/" + cotizacion.clientenumero + "-" + cotizacion.numerodocumento + ".pdf");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            (context).startActivity(intent);
                            dialog.dismiss();
                        }else if (request.getCodigoError() == 1) {
                            dialog.dismiss();
                            createDialogError("No se logro enviar el email", "onResponse").show();
                        } else if (request.getCodigoError() == 2) {
                            dialog.dismiss();
                            createDialogError(request.mensajeSistema, "onResponse").show();
                        }
                    } else {
                        dialog.dismiss();
                        createDialogError(response.message(), "onResponse").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            createDialogError(e.getMessage(), "onFailure").show();
        }
    }

    public AlertDialog createDialogProgress() {
        try {
            AlertDialog alertDialog =null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_circularloader, null);
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            return alertDialog;
        }catch (Exception e){
            createDialogError(e.getMessage(), "createDialogProgress").show();
        }
        return null;
    }
}