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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.maps.locales_detail;
import com.diamante.clubconstructor.model.Local;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class LocalListAdapter extends RecyclerView.Adapter<LocalListAdapter.ViewHolder>{
    private Context context;
    private List<Local> local;
    private LatLng latLng;
    NumberFormat distance   = new DecimalFormat("#,##0");


    public LocalListAdapter(Context context, List<Local> local, LatLng latLng){
        this.context= context;
        this.local  = local;
        this.latLng = latLng;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_local_list, parent, false);
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
            holder.textName.setText(local.get(position).getName());
            holder.textAddress.setText(local.get(position).getAddress());
            holder.textDistance.setText(distance.format(local.get(position).getDistance()) + "\nkm");
            holder.lny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, locales_detail.class);
                    Local data = local.get(position);
                    i.putExtra("Local", data);
                    i.putExtra("latLng", latLng);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
                    //((Activity) context).finish();
                }
            });
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
        if (local!=null){
            return local.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textName, textAddress, textDistance;
        private LinearLayout lny;
        public ViewHolder(View Item){
            super(Item);
            try{
                textName    = Item.findViewById(R.id.name);
                textAddress = Item.findViewById(R.id.address);
                textDistance= Item.findViewById(R.id.distance);
                lny         = Item.findViewById(R.id.lny_items);
            } catch (Exception e){
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