package com.diamante.clubconstructor.calculadora.adapter;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.util.List;

public class BrickListAdapter extends RecyclerView.Adapter<BrickListAdapter.ViewHolder>{
    private Context context;
    private List<Brick> bricks;
    public int lastCheckedPosition = -1;

    public BrickListAdapter(Context context, List<Brick> bricks){
        this.context= context;
        this.bricks = bricks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_brick_single, parent, false);
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
            String path = constantes.URL_BASE_IMAGE + bricks.get(position).getPath();
            holder.textdescripcion.setText(bricks.get(position).getDescripcionlocal());
            holder.selected.setChecked(position == lastCheckedPosition);
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
            holder.imageFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(constantes.URL_BASE_IMAGE + bricks.get(position).getFile());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    (context).startActivity(intent);
                    /*
                    Intent i = new Intent(context, calculadora_bricks_file.class);
                    i.putExtra("Brick", bricks.get(position));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);

                     */
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
        if (bricks!=null){
            return bricks.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textdescripcion;
        private RadioButton selected;
        private ImageView imageView, imageFile;
        public ViewHolder(View Item){
            super(Item);
            try {
                textdescripcion = Item.findViewById(R.id.textDescripcion);
                selected        = Item.findViewById(R.id.radioSelect);
                imageView       = Item.findViewById(R.id.imageBrick);
                imageFile       = Item.findViewById(R.id.imageFile);
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

    public Brick spinner_select(){
        try {
            if (bricks!=null){
                return bricks.get(lastCheckedPosition);
            }else {
                return null;
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: spinner_select").show();
        }
        return null;
    }
}