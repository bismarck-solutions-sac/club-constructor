package com.diamante.clubconstructor.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;
import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.agrawalsuneet.dotsloader.loaders.PullInLoader;
import com.diamante.clubconstructor.R;

public class dialog_custom extends DialogFragment {
    private String message_text, message_title;
    private int TYPE;
    private Context context;
    private dialog_success_listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (dialog_success_listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    public interface dialog_success_listener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public dialog_custom() {
        super();
        context = getContext();
    }

    public dialog_custom(String message, String title, int type) {
        this.message_text   = message;
        this.message_title  = title;
        this.TYPE           = type;
        context             = getContext();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        try {
            View view = null;
            Button btn_ok = null, btn_no = null;
            ImageView image;
            TextView textTitle = null, textBody = null;

            // Build the dialog and set up the button click handlers
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            switch (this.TYPE) {
                case 0:
                    view = LayoutInflater.from(getActivity()).inflate(
                            R.layout.dialog_success, null);
                    btn_ok = view.findViewById(R.id.buttonAction);
                    btn_ok.setText(getResources().getString(R.string.okay));

                    image = view.findViewById(R.id.imageIcon);
                    image.setImageResource(R.drawable.dialog_icon_success);

                    textTitle = view.findViewById(R.id.textTitle);
                    textBody = view.findViewById(R.id.textMessage);
                    break;
                case 1:
                    view = LayoutInflater.from(getActivity()).inflate(
                            R.layout.dialog_warning, null);
                    btn_ok = view.findViewById(R.id.buttonYes);
                    btn_ok.setText(getResources().getString(R.string.okay));

                    btn_no = view.findViewById(R.id.buttonNo);
                    btn_no.setText(getResources().getString(R.string.cancelar));

                    image = view.findViewById(R.id.imageIcon);
                    image.setImageResource(R.drawable.dialog_icon_warning);

                    textTitle = view.findViewById(R.id.textTitle);
                    textBody = view.findViewById(R.id.textMessage);
                    break;
                case 2:
                    view = LayoutInflater.from(getActivity()).inflate(
                            R.layout.dialog_error, null);
                    btn_ok = view.findViewById(R.id.buttonAction);
                    btn_ok.setText(getResources().getString(R.string.okay));

                    image = view.findViewById(R.id.imageIcon);
                    image.setImageResource(R.drawable.dialog_icon_error);

                    textTitle = view.findViewById(R.id.textTitle);
                    textBody = view.findViewById(R.id.textMessage);
                    break;
                case 3:
                    view = LayoutInflater.from(getActivity()).inflate(
                            R.layout.dialog_loader, null);
                    LazyLoader lazyLoader = view.findViewById(R.id.loader);

                    LazyLoader loader = new LazyLoader(getContext(), 30, 20, ContextCompat.getColor(getContext(), R.color.loader_selected),
                            ContextCompat.getColor(getActivity(), R.color.loader_selected),
                            ContextCompat.getColor(getActivity(), R.color.loader_selected));
                    loader.setAnimDuration(500);
                    loader.setFirstDelayDuration(100);
                    loader.setSecondDelayDuration(200);
                    lazyLoader.setInterpolator(new LinearInterpolator());
                    break;

                case 4:
                    view = LayoutInflater.from(getActivity()).inflate(
                            R.layout.dialog_circularloader, null);

                    CircularDotsLoader circularloader = new CircularDotsLoader(getContext());
                    circularloader.setDefaultColor(ContextCompat.getColor(getContext(), R.color.purple_default));
                    circularloader.setSelectedColor(ContextCompat.getColor(getContext(), R.color.purple_selected));
                    circularloader.setBigCircleRadius(80);
                    circularloader.setRadius(24);
                    circularloader.setAnimDur(300);
                    circularloader.setShowRunningShadow(true);
                    circularloader.setFirstShadowColor(ContextCompat.getColor(getContext(), R.color.pink_selected));
                    circularloader.setSecondShadowColor(ContextCompat.getColor(getContext(), R.color.pink_default));
                    break;
            }
            if (textTitle != null) {
                textTitle.setText(this.message_title);
            }
            if (textBody != null) {
                textBody.setText(this.message_text);
            }

            if (btn_ok != null) {
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDialogPositiveClick(dialog_custom.this);
                    }
                });
            }
            if (btn_no != null) {
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDialogNegativeClick(dialog_custom.this);
                    }
                });
            }

            builder.setView(view);
            final AlertDialog alertdialog = builder.create();

            alertdialog.setCancelable(false);
            alertdialog.setCanceledOnTouchOutside(false);
            if (alertdialog.getWindow() != null) {
                alertdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                //alertdialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            return alertdialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
