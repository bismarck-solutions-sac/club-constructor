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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_bricks_result;
import com.diamante.clubconstructor.model.Estimated;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CalcListAdapter extends RecyclerView.Adapter<CalcListAdapter.ViewHolder>{
    private Context context;
    private List<Estimated> detail;
    NumberFormat enteroFormat  = new DecimalFormat("#,##0");

    public CalcListAdapter(Context context, List<Estimated> detail){
        this.context= context;
        this.detail = detail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_calculadora_list, parent, false);
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
            holder.textOrden.setText(detail.get(position).getName());
            holder.textItems.setText(enteroFormat.format(detail.get(position).getDetail().size()));
            holder.textFecha.setText(detail.get(position).created_at.substring(0, 10));
            holder.btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, calculadora_bricks_result.class);
                    i.putExtra("ACTION_MODE", "Browse");
                    i.putExtra("estimated", detail.get(position));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
                    ((Activity)context).finish();
                }
            });
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onCreateViewHolder").show();
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
        TextView textOrden, textItems, textFecha;
        AppCompatButton btnVer;
        public ViewHolder(View Item){
            super(Item);
            try {
                textOrden = Item.findViewById(R.id.textNumero);
                textItems = Item.findViewById(R.id.textItems);
                textFecha = Item.findViewById(R.id.textFecha);
                btnVer    = Item.findViewById(R.id.btnDetalle);
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