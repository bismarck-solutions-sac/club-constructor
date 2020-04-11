package com.diamante.clubconstructor.calculadora.adapter;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder>{
    private Context context;
    private List<EstimatedDetail> estimatedDetail;
    NumberFormat integerFormat   = new DecimalFormat("#,##0.000");
    NumberFormat decimalFormat   = new DecimalFormat("#,##0.000");

    public ResultListAdapter(Context context, List<EstimatedDetail> estimatedDetail){
        this.context        = context;
        this.estimatedDetail= estimatedDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_brick_result, parent, false);
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
            String path = constantes.URL_BASE_IMAGE + estimatedDetail.get(position).getPath();
            holder.textArea.setText(Html.fromHtml( "Área " + integerFormat.format(estimatedDetail.get(position).getArea()) + " m" + "<sup>2</sup>"));
            holder.lnyAdicionales.setVisibility(View.GONE);
            holder.lnyCemento.setVisibility(View.GONE);
            holder.lnyArena.setVisibility(View.GONE);
            holder.lnyPiedra.setVisibility(View.GONE);
            holder.textTotal.setText( integerFormat.format(estimatedDetail.get(position).total_brick));
            holder.textBrick.setText(estimatedDetail.get(position).brick_name);
            if (estimatedDetail.get(position).getBag_require()==1){
                holder.lnyAdicionales.setVisibility(View.VISIBLE);
                holder.lnyCemento.setVisibility(View.VISIBLE);
                holder.textCemento.setText("");
                holder.textCemento.setText(Html.fromHtml( decimalFormat.format(estimatedDetail.get(position).getBag_cement() )+ " m" + "<sup>3</sup>"));
            }
            if (estimatedDetail.get(position).getMortar_require()==1){
                holder.lnyAdicionales.setVisibility(View.VISIBLE);
                holder.lnyArena.setVisibility(View.VISIBLE);
                holder.textArena.setText("");
                holder.textArena.setText(Html.fromHtml( decimalFormat.format(estimatedDetail.get(position).getMortar()) + " m" + "<sup>3</sup>"));
            }
            if (estimatedDetail.get(position).getRock_require()==1){
                holder.lnyAdicionales.setVisibility(View.VISIBLE);
                holder.lnyPiedra.setVisibility(View.VISIBLE);
                holder.textPiedra.setText("");
                holder.textPiedra.setText(Html.fromHtml( decimalFormat.format(estimatedDetail.get(position).getRock()) + " m" + "<sup>3</sup>"));
            }
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
        if (estimatedDetail!=null){
            return estimatedDetail.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textBrick, textCemento, textArena, textPiedra, textTotal, textArea;
        private LinearLayout lnyCemento, lnyArena, lnyPiedra, lnyAdicionales;
        public ViewHolder(View Item){
            super(Item);
            try {
                imageView = Item.findViewById(R.id.imageBrick);
                textBrick = Item.findViewById(R.id.textBrick);
                textTotal = Item.findViewById(R.id.textTotal);
                textArea = Item.findViewById(R.id.textArea);

                textCemento = Item.findViewById(R.id.textCemento);
                textArena = Item.findViewById(R.id.textArena);
                textPiedra = Item.findViewById(R.id.textPiedra);
                lnyCemento = Item.findViewById(R.id.lny_cemento);
                lnyArena = Item.findViewById(R.id.lny_arena);
                lnyPiedra = Item.findViewById(R.id.lny_piedra);
                lnyAdicionales = Item.findViewById(R.id.lny_adicionales);
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