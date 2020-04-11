package com.diamante.clubconstructor.club.adapters;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.model.Eventos;
import com.diamante.clubconstructor.model.JobsPublication;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosListAdapter extends RecyclerView.Adapter<EventosListAdapter.ViewHolder>{
    private Context context;
    private List<Eventos> eventos;
    private String rootImage;
    private globals global = globals.getInstance();

    public EventosListAdapter(Context context, List<Eventos> eventos){
        this.context    = context;
        this.eventos    = eventos;
        rootImage       = constantes.URL_BASE_IMAGE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View ItemView = null;
            ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_eventos_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(ItemView);
            return viewHolder;
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            String path = rootImage + eventos.get(position).getPath();
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .error(R.drawable.app_icon_notimage)
                    .into(holder.imageView);
            holder.title.setText(eventos.get(position).title);
            holder.name.setText(eventos.get(position).name_event);
            holder.address.setText(eventos.get(position).address);
            holder.start_at.setText(eventos.get(position).start_at);
            holder.end_at.setText(eventos.get(position).end_at);
            if (eventos.get(position).rows>0){
                holder.btn_ok.setEnabled(false);
            }else{
                holder.btn_ok.setEnabled(true);
            }
            holder.btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update(eventos.get(position), v);
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
        if (eventos!=null){
            return eventos.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title, name, address, start_at, end_at;
        private Button btn_ok;
        public ViewHolder(View Item){
            super(Item);
            try {
                imageView   = Item.findViewById(R.id.imgView);
                title       = Item.findViewById(R.id.title);
                name        = Item.findViewById(R.id.name);
                address     = Item.findViewById(R.id.address);
                start_at    = Item.findViewById(R.id.start_at);
                end_at      = Item.findViewById(R.id.end_at);
                btn_ok      = Item.findViewById(R.id.btn_ok);
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

    private void Update(Eventos evento, View v){
        RequestParameter parameter = new RequestParameter();
        parameter.user      = global.getUser();
        parameter.evento    = evento;
        MethodWS methodWS   = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.eventosAdd (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                            v.setEnabled(false);
                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), "Validación").show();
                        }
                    }else{
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }
}