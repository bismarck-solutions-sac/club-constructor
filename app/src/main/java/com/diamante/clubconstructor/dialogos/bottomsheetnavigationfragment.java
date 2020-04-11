package com.diamante.clubconstructor.dialogos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.diamante.clubconstructor.BuildConfig;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_list;
import com.diamante.clubconstructor.contacto.contacto;
import com.diamante.clubconstructor.cotizacion.cotizacion_list;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.club.clublevel;
import com.diamante.clubconstructor.login.splash;
import com.diamante.clubconstructor.maps.locales;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class bottomsheetnavigationfragment extends BottomSheetDialogFragment {

    private ImageView closeButton;
    private globals global = globals.getInstance();
    private SharedPreferences sharedPreferences;

    public static bottomsheetnavigationfragment newInstance() {
        Bundle args = new Bundle();
        bottomsheetnavigationfragment fragment = new bottomsheetnavigationfragment();
        fragment.setArguments(args);
        return fragment;
    }
    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //check the slide offset and change the visibility of close button
            if (slideOffset > 0.5) {
                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {

        super.setupDialog(dialog, style);

        try {

            sharedPreferences       = getContext().getSharedPreferences(constantes.PREFERENCE_NAME, Context.MODE_PRIVATE);
            View contentView        = View.inflate(getContext(), R.layout.bottom_navigation_menu, null);
            TextView textUser       = contentView.findViewById(R.id.user_name);
            TextView textName       = contentView.findViewById(R.id.user_email);
            TextView textCotizacion = contentView.findViewById(R.id.cotizacion);
            TextView textCalculos   = contentView.findViewById(R.id.calculos);
            TextView textUbicanos   = contentView.findViewById(R.id.ubicanos);
            TextView textPuntos     = contentView.findViewById(R.id.puntos);
            TextView textContacto   = contentView.findViewById(R.id.contactanos);
            TextView textVersion    = contentView.findViewById(R.id.text_version);
            CircleImageView profile = contentView.findViewById(R.id.profile_image);
            TextView textClose      = contentView.findViewById(R.id.close);

            textVersion.setText("Version: " + BuildConfig.VERSION_NAME);
            if (global.getUser()!=null){
                textUser.setText(global.getUser().full_name);
                textName.setText(global.getUser().email);

                if (global.getUser().getPath().length()!=0){
                    Glide.with(getContext())
                            .load(constantes.URL_BASE_IMAGE + global.getUser().getPath())
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .error(R.drawable.app_icon_profile)
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
                            .into(profile);
                }
            }

            textCotizacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), cotizacion_list.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    dismiss();
                }
            });

            textCalculos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), calculadora_list.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    dismiss();
                }
            });

            textUbicanos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), locales.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    dismiss();
                }
            });

            textPuntos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), clublevel.class);
                    i.putExtra("user", global.getUser());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    dismiss();
                }
            });

            textContacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), contacto.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    dismiss();
                }
            });

            textClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    global.setUser(null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent i = new Intent(getContext(), splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    (getActivity()).finish();
                    dismiss();
                }
            });

            dialog.setContentView(contentView);

            closeButton = contentView.findViewById(R.id.close_image_view);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            //Set the coordinator layout behavior
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            //Set callback
            if (behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            }
            /*
            if (dialog.getWindow() !=null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogZoom_back;
            }
             */
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
