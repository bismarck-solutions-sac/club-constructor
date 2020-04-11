package com.diamante.clubconstructor.adapters;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_brick_type;
import com.diamante.clubconstructor.club.clubcapacitacion;
import com.diamante.clubconstructor.club.clublevel;
import com.diamante.clubconstructor.club.clubmenu;
import com.diamante.clubconstructor.club.clubnoticias;
import com.diamante.clubconstructor.club.clubpromocion;
import com.diamante.clubconstructor.club.clubreward;
import com.diamante.clubconstructor.club.clubtrabajo;
import com.diamante.clubconstructor.contacto.contacto;
import com.diamante.clubconstructor.cotizacion.cotizacion_step0;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.maps.locales;
import com.diamante.clubconstructor.model.Menu;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;

import java.util.List;

import okhttp3.internal.Internal;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder>{
    private Context context;
    private List<Menu> menu;
    private String rootImage;
    private User user;
    private functions function = functions.getInstance();


    public MenuListAdapter(Context context, List<Menu> menu, User user){
        this.context    = context;
        this.menu       = menu;
        this.user       = user;
        this.rootImage  = constantes.URL_BASE_IMAGE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_menu, parent, false);
            ViewHolder viewHolder = new ViewHolder(ItemView);
            return viewHolder;
        }catch (Exception e){
            createDialogError("Opcion no implementada...", "Error: onCreateViewHolder").show();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            String path = rootImage + menu.get(position).getPath();
            holder.textTitle.setText(menu.get(position).getName());
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .error(R.drawable.app_icon_notimage)
                    .into(holder.imageView);
            holder.cardView.setEnabled(true);
            if (user!=null){
                if (user.id<0){
                    holder.cardView.setEnabled(false);
                    holder.cardView.setCardBackgroundColor(Color.LTGRAY);
                    //holder.imageView.setImageDrawable((context).getResources().getDrawable(R.drawable.app_icon_image_block));
                }
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =null;
                    switch (menu.get(position).slug){
                        case "mis-calculos":
                            i = new Intent(context, calculadora_brick_type.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "cotizar":
                            i = new Intent(context, cotizacion_step0.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "donde-comprar":
                            i = new Intent(context, locales.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "contactenos":
                            i = new Intent(context, contacto.class);
                            i.putExtra("user", user);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "club-constructor":
                            if  (!function.f_read_parametros_texto("999999", "VM", "CCACCCLUB").equals(user.getProfesion())){
                                createDialogError("Opción no disponible para su perfil, comuníquese con servicio al cliente.", context.getString(R.string.app_name) ).show();
                                return;
                            }
                            i = new Intent(context, clubmenu.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("user", user);
                            break;
                        case "mis-puntos":
                            i = new Intent(context, clublevel.class);
                            i.putExtra("user", user);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "promociones-especiales":
                            i = new Intent(context, clubpromocion.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "portal-de-trabajo":
                            i = new Intent(context, clubtrabajo.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "noticias":
                            i = new Intent(context, clubnoticias.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "capacitacion-virtual":
                            i = new Intent(context, clubcapacitacion.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;

                        case "premios":
                            i = new Intent(context, clubreward.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            break;
                        case "invitar":
                            i = share_app();
                            break;
                    }
                    if (i!=null){
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        (context).startActivity(i);
                    }else{
                        createDialogError("Opcion no implementada...", "Error: onBindViewHolder").show();
                    }
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onBindViewHolder").show();
        }
    }

    private Intent share_app() {
        try {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String aux;
            aux = function.f_read_parametros_explicacion("999999", "VM", "CCSHAREAPP");
            aux += "\n\nDescárgala gratis desde\n\n";
            aux += "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, aux);
            return i;
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: share_app").show();
        }
        return null;
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
        if (menu!=null){
            return menu.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private ImageView imageView;
        private CardView cardView;
        public ViewHolder(View Item){
            super(Item);
            try {
                textTitle   = Item.findViewById(R.id.textTitle);
                imageView   = Item.findViewById(R.id.imgView);
                cardView     = Item.findViewById(R.id.cardview);
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