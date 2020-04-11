package com.diamante.clubconstructor.cotizacion.adapter;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_bricks_file;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.CotizacionDetalle;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CotizacionCartAdapter extends RecyclerView.Adapter<CotizacionCartAdapter.ViewHolder> implements dialog_custom.dialog_success_listener{
    private Context context;
    private Cotizacion cotizacion;
    private List<CotizacionDetalle> detail;
    NumberFormat decimalFormat  = new DecimalFormat("#,##0.00");
    NumberFormat cantidadFormat = new DecimalFormat("#,##0.000");

    public CotizacionCartAdapter(Context context, Cotizacion cotizacion){
        this.context    = context;
        this.cotizacion = cotizacion;
        this.detail     = cotizacion.detalle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = null;
        ViewHolder viewHolder =null;
        try {
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cotizacion_cart, parent, false);
            viewHolder = new ViewHolder(ItemView);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            String path = constantes.URL_BASE_IMAGE +  detail.get(position).path;
            holder.textbrick.setText(detail.get(position).descripcion);
            holder.textcantidad.setText(cantidadFormat.format(detail.get(position).getCantidadpedida()));
            holder.textprecio.setText(decimalFormat.format(detail.get(position).getPreciounitario()));
            holder.textflete.setText(decimalFormat.format(detail.get(position).getMontoflete()));
            holder.textmonto.setText(decimalFormat.format(detail.get(position).getMonto()));
            Glide.with(context).load(path)
                    .error(R.drawable.app_icon_brick)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.imageView);
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
        if (cotizacion.detalle!=null){
            return cotizacion.detalle.size();
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
        private TextView textbrick, textcantidad, textprecio, textmonto, textflete;
        private ImageView imageView;

        public ViewHolder(View Item){
            super(Item);
            textbrick       = Item.findViewById(R.id.textbrick);
            textcantidad    = Item.findViewById(R.id.textcantidad);
            textprecio      = Item.findViewById(R.id.textprecio);
            textmonto       = Item.findViewById(R.id.textmonto);
            textflete       = Item.findViewById(R.id.textflete);
            imageView       = Item.findViewById(R.id.image);
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