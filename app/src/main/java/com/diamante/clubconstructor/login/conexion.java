package com.diamante.clubconstructor.login;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.diamante.clubconstructor.R;

public class conexion extends AppCompatActivity {

    private TextView text_title;
    private Button btn_close;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ls_error;
        setContentView(R.layout.activity_conexion);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Bundle bundle = this.getIntent().getExtras();
        text_title  = findViewById(R.id.text_title);
        btn_close   = findViewById(R.id.btn_close);

        text_title.setText(getResources().getString(R.string.ErrorDatos));
        if (bundle!=null){
            ls_error = bundle.getString("ErrorCnx");
            if (ls_error.length()!=0){
                text_title.setText(ls_error);
            }
        }
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
