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
import android.widget.Toast;

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
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class BrickListAdapter extends RecyclerView.Adapter<BrickListAdapter.ViewHolder> implements dialog_custom.dialog_success_listener{
    private Context context;
    private List<Brick> bricks;
    NumberFormat decimalFormat   = new DecimalFormat("#,##0.00");

    public BrickListAdapter(Context context, List<Brick> bricks){
        this.context= context;
        this.bricks = bricks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ItemView = null;
        ViewHolder viewHolder =null;
        try {
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_brick_multiple, parent, false);
            viewHolder = new ViewHolder(ItemView);
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            String path = constantes.URL_BASE_IMAGE + bricks.get(position).getPath();
            holder.textdescripcion.setText(bricks.get(position).getDescripcionlocal());
            holder.edt_cantidad.setText(decimalFormat.format(bricks.get(position).getCantidad()));
            holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    bricks.get(position).selected = isChecked;
                    holder.edt_cantidad.setEnabled(isChecked);
                }
            });
            Glide.with(context).load(path)
                    .error(R.drawable.app_icon_brick)
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
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(holder.imageView);

            holder.imageFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, calculadora_bricks_file.class);
                    i.putExtra("Brick", bricks.get(position));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
                }
            });
            holder.edt_cantidad.setSelectAllOnFocus(true);
            holder.edt_cantidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.edt_cantidad.setSelection(0, holder.edt_cantidad.getText().length());
                    holder.edt_cantidad.selectAll();
                }
            });
            holder.edt_cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    double cantidad;
                    try{
                        if (s.toString().trim().equals("")){
                            cantidad =0;
                        }else{
                            cantidad = Double.valueOf(s.toString());
                        }
                        bricks.get(position).cantidad=cantidad;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
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
        if (bricks!=null){
            return bricks.size();
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
        private TextView textdescripcion;
        private AppCompatCheckBox selected;
        private ImageView imageView, imageFile;
        private EditText edt_cantidad;
        public ViewHolder(View Item){
            super(Item);
            textdescripcion = Item.findViewById(R.id.textDescripcion);
            selected        = Item.findViewById(R.id.checkSelect);
            imageView       = Item.findViewById(R.id.imageBrick);
            imageFile       = Item.findViewById(R.id.imageFile);
            edt_cantidad    = Item.findViewById(R.id.editCantidad);
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