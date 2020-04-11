package com.diamante.clubconstructor.faqs.adapter;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_brick_type;
import com.diamante.clubconstructor.contacto.contacto;
import com.diamante.clubconstructor.cotizacion.cotizacion_step0;
import com.diamante.clubconstructor.maps.locales;
import com.diamante.clubconstructor.model.Faqs;
import com.diamante.clubconstructor.model.Menu;
import com.diamante.clubconstructor.util.constantes;

import java.util.List;

public class FaqsListAdapter extends RecyclerView.Adapter<FaqsListAdapter.ViewHolder>{
    private Context context;
    private List<Faqs> faqs;

    public FaqsListAdapter(Context context, List<Faqs> faqs){
        this.context= context;
        this.faqs   = faqs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ItemView = null;
        ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_faqs_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(ItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Spanned spanned;
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                spanned = HtmlCompat.fromHtml(String.valueOf(faqs.get(position).answer), HtmlCompat.FROM_HTML_MODE_COMPACT);
            } else {
                spanned = Html.fromHtml(String.valueOf(faqs.get(position).answer));
            }
            holder.textQuestion.setText(faqs.get(position).question);
            holder.textAnswer.setText(spanned);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.textAnswer.getVisibility()==View.GONE){
                        holder.textAnswer.setVisibility(View.VISIBLE);
                        holder.imageView.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.app_icon_showless));
                    }else{
                        holder.textAnswer.setVisibility(View.GONE);
                        holder.imageView.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.app_icon_showmore));
                    }
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
        if (faqs!=null){
            return faqs.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textQuestion, textAnswer;
        private ImageView imageView;
        public ViewHolder(View Item){
            super(Item);
            textQuestion    = Item.findViewById(R.id.question);
            textAnswer      = Item.findViewById(R.id.answer);
            imageView       = Item.findViewById(R.id.image);
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
    }
}