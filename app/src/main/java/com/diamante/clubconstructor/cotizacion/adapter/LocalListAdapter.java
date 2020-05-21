package com.diamante.clubconstructor.cotizacion.adapter;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.cotizacion_estructura;
import com.diamante.clubconstructor.cotizacion.cotizacion_datos;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.Local;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class LocalListAdapter extends RecyclerView.Adapter<LocalListAdapter.ViewHolder>{
    private Context context;
    private List<Local> local;
    private Estimated estimated;
    private List<EstimatedDetail> estimatedDetail;
    NumberFormat distance   = new DecimalFormat("#,##0");

    public LocalListAdapter(Context context, List<Local> local, Estimated estimated){
        this.context    = context;
        this.local      = local;
        this.estimated  = estimated;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ItemView = null;
        ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_local_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(ItemView);
        return viewHolder;
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
                    Intent i = null;

                    if (estimated != null){
                        if (estimated.getDetail().size()==0){
                            i = new Intent(context, cotizacion_estructura.class);
                        }else{
                            i = new Intent(context, cotizacion_datos.class);
                        }
                    }else{
                        i = new Intent(context, cotizacion_estructura.class);
                    }
                    i.putExtra("estimated", estimated);
                    i.putExtra("agencia", local.get(position));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
                    ((Activity) context).finish();
                }
            });
        }catch (Exception e){
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
            textName    = Item.findViewById(R.id.name);
            textAddress = Item.findViewById(R.id.address);
            textDistance= Item.findViewById(R.id.distance);
            lny         = Item.findViewById(R.id.lny_items);

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
}