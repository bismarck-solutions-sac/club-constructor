package com.diamante.clubconstructor.faqs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.CotListAdapter;
import com.diamante.clubconstructor.faqs.adapter.FaqsListAdapter;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class faqs extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textMessage;
    private RecyclerView recyclerView;
    private Context context;
    private FaqsListAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        inicializa();
    }

    private void inicializa() {
        try {
            context         = this;
            progressBar     = findViewById(R.id.progress_bar);
            textMessage     = findViewById(R.id.textMessage);
            recyclerView    = findViewById(R.id.recycler);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration( new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            loadData();

        }catch (Exception e){

        }
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.faqs();
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            adapter = new FaqsListAdapter(context, request.faqs_list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (request.faqs_list ==null ){
                                textMessage.setVisibility(View.VISIBLE);
                            }else {
                                if (request.faqs_list.size()==0){
                                    textMessage.setVisibility(View.VISIBLE);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        } else if(request.getCodigoError()==2){
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText(request.mensajeSistema);
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText(response.body().mensajeSistema);
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(t.getMessage());
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText(e.getMessage());
        }
    }
}
