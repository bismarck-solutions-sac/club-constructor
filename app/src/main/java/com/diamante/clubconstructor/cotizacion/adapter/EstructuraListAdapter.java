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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_metraje;
import com.diamante.clubconstructor.cotizacion.cotizacion_step2;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class EstructuraListAdapter extends RecyclerView.Adapter<EstructuraListAdapter.ViewHolder> implements dialog_custom.dialog_success_listener{
    private Context context;
    private List<GeneralSpinner> estructura;
    private List<Brick> bricks_add;
    private Local local;
    private String rootImage;

    public EstructuraListAdapter(Context context, List<GeneralSpinner> estructura, List<Brick> bricks_add, Local local){
        this.context    = context;
        this.estructura = estructura;
        this.bricks_add = bricks_add;
        this.local      = local;
        rootImage       = constantes.URL_BASE_IMAGE + "estructura/";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ItemView = null;
        ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_estructura, parent, false);
        ViewHolder viewHolder = new ViewHolder(ItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            String path = rootImage + estructura.get(position).getId() + ".png";
            holder.textTitle.setText(estructura.get(position).getDescripcion());
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, cotizacion_step2.class);
                    i.putExtra("brick_type", estructura.get(position));
                    i.putExtra("bricks_add", (Serializable) bricks_add);
                    i.putExtra("agencia", local);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
                    ((Activity)context).finish();
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
        if (estructura!=null){
            return estructura.size();
        }
        return 0;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private ImageView imageView;
        private CardView cardView;
        public ViewHolder(View Item){
            super(Item);
            textTitle   = Item.findViewById(R.id.textTitle);
            imageView   = Item.findViewById(R.id.imgView);
            cardView    = Item.findViewById(R.id.cardview);
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