package com.diamante.clubconstructor.club.adapters;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.club.clublevel;
import com.diamante.clubconstructor.club.clubpromocion_detail;
import com.diamante.clubconstructor.model.Promocion;
import com.diamante.clubconstructor.model.PromocionCategoria;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PromocionListAdapter extends RecyclerView.Adapter<PromocionListAdapter.ViewHolder>{
    private Context context;
    private List<Promocion> promocions;
    private PromocionCategoria categoria;
    private String rootImage;
    NumberFormat formato  = new DecimalFormat("#,##0.00");

    public PromocionListAdapter(Context context, List<Promocion> promocions, PromocionCategoria categoria){
        this.context    = context;
        this.promocions = promocions;
        this.categoria  = categoria;
        rootImage   = constantes.URL_BASE_IMAGE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = LayoutInflater.from(context).inflate(R.layout.items_promocion_list, parent, false);
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
            String path = rootImage + promocions.get(position).getPath();
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .error(R.drawable.app_icon_notimage)
                    .into(holder.imgView);
            holder.imgView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.textName.setTextColor(Color.WHITE);
                }
            },500);
            holder.textName.setText(promocions.get(position).title);
            if (promocions.get(position).getType().equals("D")){
                holder.textAmmount.setText(formato.format(promocions.get(position).amount) + "%");
            }
            holder.textAmmount.setBackgroundColor(Color.parseColor(categoria.getColor_text()));
            holder.textAmmount.setTextColor(Color.WHITE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, clubpromocion_detail.class);
                    i.putExtra("Promocion", promocions.get(position));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    (context).startActivity(i);
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
        if (promocions!=null){
            return promocions.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textName, textAmmount;
        private ImageView imgView;
        private CardView cardView;
        public ViewHolder(View Item){
            super(Item);
            try {
                imgView         = Item.findViewById(R.id.imgView);
                textName        = Item.findViewById(R.id.name);
                textAmmount     = Item.findViewById(R.id.ammount);
                cardView        = Item.findViewById(R.id.cardview);
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