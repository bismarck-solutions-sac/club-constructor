package com.diamante.clubconstructor.login.adaptadores;
import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.model.UserEmpresa;

import java.util.List;

public class UserEmpresaAdapter extends RecyclerView.Adapter<UserEmpresaAdapter.ViewHolder>{
    private Context context;
    private List<UserEmpresa> userEmpresa;
    public int lastCheckedPosition =-1;

    public UserEmpresaAdapter(Context context, List<UserEmpresa> userEmpresa){
        this.context    = context;
        this.userEmpresa= userEmpresa;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = null;
        ViewHolder viewHolder =null;
        try {
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_empresa_single, parent, false);
            viewHolder = new ViewHolder(ItemView);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{

            holder.textRazonSocial.setText(userEmpresa.get(position).getRazonsocial());
            holder.textRUC.setText(userEmpresa.get(position).getRuc());
            holder.textDireccion.setText(userEmpresa.get(position).getDireccion());
            holder.selected.setChecked(position == lastCheckedPosition);
            if (lastCheckedPosition == position){
                holder.selected.setChecked(true);
            }else{
                holder.selected.setChecked(false);
            }
            holder.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCheckedPosition = position;
                    notifyDataSetChanged();
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
        if (userEmpresa!=null){
            return userEmpresa.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textRazonSocial, textRUC, textDireccion;
        private RadioButton selected;
        public ViewHolder(View Item){
            super(Item);
            try {
                textRazonSocial = Item.findViewById(R.id.textRazonSocial);
                textRUC         = Item.findViewById(R.id.textRuc);
                textDireccion   = Item.findViewById(R.id.textDireccion);
                selected        = Item.findViewById(R.id.radioSelect);
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

    public UserEmpresa select(){
        try {
            if (userEmpresa!=null){
                return userEmpresa.get(lastCheckedPosition);
            }else {
                return null;
            }
        }catch (Exception e){

        }
        return null;
    }
}