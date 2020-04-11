package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.main;

public class clubpuntos extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubpuntos);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        inicializa();
    }

    private void inicializa() {
        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
        }catch (Exception e){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            return super.onOptionsItemSelected(item);
        }
    }
}
