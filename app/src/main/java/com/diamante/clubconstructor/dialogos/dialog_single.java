package com.diamante.clubconstructor.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.SpinnerSingleAdapter;
import com.diamante.clubconstructor.model.GeneralSpinner;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class dialog_single extends DialogFragment {
    private String message_title;
    private Context context;
    private dialog_single_listener listener;
    private List<GeneralSpinner> spinnerList;
    private SpinnerSingleAdapter spinnerSingleAdapter;
    private int POSITION_SELECTED=-1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (dialog_single_listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    public interface dialog_single_listener {
        void onDialogPositiveClick(DialogFragment dialog, GeneralSpinner spinner);
    }

    public dialog_single() {
        super();
        context = getContext();
    }

    public dialog_single(String title, List<GeneralSpinner> spinners, int position) {
        this.message_title      = title;
        this.spinnerList        = spinners;
        context                 = getContext();
        this.POSITION_SELECTED  = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        try {
            View view = null;
            Button btn_ok = null;
            ImageView image;
            TextView textTitle = null;
            RecyclerView recyclerView = null;

            // Build the dialog and set up the button click handlers
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.dialog_single, null);

            btn_ok = view.findViewById(R.id.buttonAction);
            image = view.findViewById(R.id.imageIcon);
            textTitle = view.findViewById(R.id.textTitle);
            recyclerView = view.findViewById(R.id.recycler);

            btn_ok.setText(getResources().getString(R.string.okay));
            image.setImageResource(R.drawable.dialog_icon_success);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            if (spinnerList != null) {
                spinnerSingleAdapter = new SpinnerSingleAdapter(context, spinnerList);
                recyclerView.setAdapter(spinnerSingleAdapter);
            }

            if (textTitle != null) {
                textTitle.setText(this.message_title);
            }

            spinnerSingleAdapter.lastCheckedPosition = this.POSITION_SELECTED;

            if (btn_ok != null) {
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spinnerSingleAdapter.lastCheckedPosition >= 0) {
                            GeneralSpinner item = spinnerList.get(spinnerSingleAdapter.lastCheckedPosition);
                            listener.onDialogPositiveClick(dialog_single.this, item);
                        } else {
                            Toast.makeText(getActivity(), "Debe seleccionar un valor", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            builder.setView(view);
            final AlertDialog alertdialog = builder.create();

            alertdialog.setCancelable(false);
            alertdialog.setCanceledOnTouchOutside(false);
            if (alertdialog.getWindow() != null) {
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
