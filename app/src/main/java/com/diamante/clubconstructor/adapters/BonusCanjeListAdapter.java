package com.diamante.clubconstructor.adapters;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_brick_type;
import com.diamante.clubconstructor.contacto.contacto;
import com.diamante.clubconstructor.cotizacion.cotizacion_step0;
import com.diamante.clubconstructor.maps.locales;
import com.diamante.clubconstructor.model.BonusCanje;
import com.diamante.clubconstructor.model.Menu;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class BonusCanjeListAdapter extends RecyclerView.Adapter<BonusCanjeListAdapter.ViewHolder>{
    private Context context;
    private List<BonusCanje> bonusCanjes;
    NumberFormat formato  = new DecimalFormat("#,##0.00");

    public BonusCanjeListAdapter(Context context, List<BonusCanje> bonusCanjes){
        this.context    = context;
        this.bonusCanjes= bonusCanjes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bonuscanje_list, parent, false);
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
            holder.textFecha.setText(bonusCanjes.get(position).fecha.substring(0,10));
            holder.textDocumento.setText(bonusCanjes.get(position).referenciatipodocumento + "-" + bonusCanjes.get(position).referenciadocumento);
            holder.textPuntos.setText(formato.format(bonusCanjes.get(position).puntajeconsumido));
            if (bonusCanjes.get(position).ingresoegresoflag.equals("E")){
                holder.imageView.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.app_icon_egreso));
            }else{
                holder.imageView.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.app_icon_ingreso));
            }
        }catch (Exception e){
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
        if (bonusCanjes!=null){
            return bonusCanjes.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textFecha, textDocumento, textPuntos;
        private ImageView imageView;
        public ViewHolder(View Item){
            super(Item);
            try {
                textFecha       = Item.findViewById(R.id.fecha);
                textDocumento   = Item.findViewById(R.id.documento);
                textPuntos      = Item.findViewById(R.id.puntos);
                imageView       = Item.findViewById(R.id.image);
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
            e.printStackTrace();
        }
        return null;
    }
}