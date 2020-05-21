package com.diamante.clubconstructor;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.adapters.MenuListAdapter;
import com.diamante.clubconstructor.club.club_puntos;
import com.diamante.clubconstructor.dialogos.bottomsheetnavigationfragment;
import com.diamante.clubconstructor.faqs.faqs;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.login.image;
import com.diamante.clubconstructor.login.profile;
import com.diamante.clubconstructor.model.Level;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class main extends AppCompatActivity {

    public static final int PICK_IMAGE          = 100;
    public static final int PERMISSAO_REQUEST   = 2;

    private AlertDialog dialogProgress;
    private BottomAppBar bottomAppBar;
    private RecyclerView recyclerView;
    private RelativeLayout btn_llamar;
    private Context context;
    private MenuListAdapter menuListAdapter;
    private TextView textUser, textCategoria, textHelp, textpuntos;
    private CircleImageView cimgprofile, cimglevel, cigpuntos, cimgedit;
    private globals global = globals.getInstance();
    private functions function = functions.getInstance();

    NumberFormat decimalFormat   = new DecimalFormat("#,##0.00");

    private int dstWith=1024, dstHeight=768;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            Log.v("main", "Inside of onRestoreInstanceState");
            global.setUser((User) savedInstanceState.getSerializable("user"));
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("user", global.getUser());
        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            recyclerView.setAdapter(menuListAdapter);
        }catch (Exception e){
            createDialogError(e.getMessage(), "onResume").show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("user") != null))
        {
            global.setUser((User) savedInstanceState.getSerializable("user"));
        }
        inicializa();

        if (global!=null) {
            if (global.getUser().getId() > 0) {
                try {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                        String token = instanceIdResult.getToken();
                        UpdateToken(token);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
                if (data==null){
                    return;
                }
                Bundle extras = data.getExtras();
                Bitmap imageBitmap =null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Uri selectedImage = data.getData();
                    imageBitmap = MediaStore.Images.Media. getBitmap ( this .getContentResolver() , selectedImage) ;
                    cimgprofile.setImageBitmap(imageBitmap);
                } else {
                    imageBitmap = extras.getParcelable("data");
                    cimgprofile.setImageBitmap(imageBitmap);
                }
                try {
                    cimgprofile.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (cimgprofile.getDrawable() instanceof BitmapDrawable) {
                                Drawable dr     = cimgprofile.getDrawable();
                                Bitmap bitmap   = ((BitmapDrawable)dr.getCurrent()).getBitmap();
                                ConvertBitmapToString upload = new ConvertBitmapToString();
                                upload.execute(bitmap);
                            }
                        }
                    },500);
                } catch (Exception e) {
                    createDialogError(e.getMessage(), "onActivityResult").show();
                }
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "onResume").show();
        }
    }

    private void UpdateToken(String token){
        try {
            if (global.getUser().getToken().equals(token)){
                return;
            }
            global.getUser().setToken(token);
            RequestParameter parameter = new RequestParameter();
            parameter.user= global.getUser();
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result = methodWS.setuserToken (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){

                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
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
            createDialogError(e.getMessage(), "Error: UpdateToken").show();
        }
    }

    private class ConvertBitmapToString extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected void onPreExecute() {
            cimgprofile.setEnabled(false);
            dialogProgress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String encodedImage = "";
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                params[0].compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] b = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                return encodedImage;
            }catch (Exception e){

            }
            return encodedImage;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.length()!=0){
                global.getUser().setBase64String(result);
                UpdateImage();
            }
        }
    }

    private void inicializa() {
        try {
            context             = this;
            dialogProgress      = createDialogProgress();
            bottomAppBar        = findViewById(R.id.bottom_app_bar);
            textUser            = findViewById(R.id.textUser);
            cimgprofile         = findViewById(R.id.cimgprofile);
            btn_llamar          = findViewById(R.id.btn_llamar);
            textCategoria       = findViewById(R.id.textcategoria);
            textHelp            = findViewById(R.id.textHelp);
            textpuntos          = findViewById(R.id.textpuntos);
            cimglevel           = findViewById(R.id.cimglevel);
            cigpuntos           = findViewById(R.id.cmigpuntos);
            cimgedit            = findViewById(R.id.cimgedit);

            cimgedit.bringToFront();
            setSupportActionBar(bottomAppBar);

            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                }else{
                    ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST );
                }
            }

            cimgprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, image.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("user", global.getUser());
                    startActivity(i);
                }
            });
            cimgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (global.getUser().id>0){
                        Intent photoIntent;
                        photoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(photoIntent, PICK_IMAGE);
                    }
                }
            });

            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open bottom sheet
                    BottomSheetDialogFragment bottomSheetDialogFragment = bottomsheetnavigationfragment.newInstance();
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                }
            });
            btn_llamar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(main.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                            return;
                        }
                    }
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setData(Uri.parse(constantes.CALL_PHONE_NUMBER));
                    startActivity(i);
                }
            });
            textHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, faqs.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            cigpuntos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, club_puntos.class);
                    i.putExtra("user", global.getUser());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            recyclerView        = findViewById(R.id.recycler);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2,RecyclerView.VERTICAL,false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setClipToPadding(true);

            textUser.setText("¡Hola " + global.user.first_name + "!");

            setup_level();

            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "inicializa").show();
        }
    }

    private void setup_level() {
        try {
            if (global.user.path.length()!=0){
                Glide.with(context)
                        .load(constantes.URL_BASE_IMAGE + global.user.path)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .error(R.drawable.app_icon_profile)
                        .into(cimgprofile);
            }
            List<Level> level_list = global.getUser().level_list;
            if (level_list!=null){
                int rows = level_list.size();
                double points = global.getUser().getPuntos();
                for (int i=0; i< rows; i++){
                    if (points >= level_list.get(i).value_init){
                        global.getUser().setLevel(level_list.get(i).id);
                        Glide.with(context).load(constantes.URL_BASE_IMAGE + level_list.get(i).path).into(cimglevel);
                        textCategoria.setText(level_list.get(i).name);
                    }
                }
            }
            textpuntos.setText(decimalFormat.format(global.getUser().puntos));
        }catch (Exception e){
            createDialogError(e.getMessage(), "inicializa").show();
        }
    }

    private void loadData() {
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getMenuList();
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            menuListAdapter = new MenuListAdapter(context, request.menu, global.getUser());
                            recyclerView.setAdapter(menuListAdapter);
                            menuListAdapter.notifyDataSetChanged();
                            dialogProgress.dismiss();
                        } else if (request.getCodigoError() == 2) {
                            dialogProgress.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        }
                    } else {
                        dialogProgress.dismiss();
                        createDialogError(response.message(), "onResponse").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "onFailure").show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
        }catch (Exception e){
            createDialogError(e.getMessage(), "onCreateOptionsMenu").show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        try {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    i = new Intent(context, setting.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    break;

                case R.id.action_account:
                    if (global.getUser().id>0){
                        i = new Intent(context, profile.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("user", global.getUser());
                        startActivity(i);
                    }
                    break;

                case R.id.action_www:
                    String dato = function.f_read_parametros_explicacion("999999", "VM", "CPAGINAWEB");
                    Uri uri     = Uri.parse(dato);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    try{
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "onFailure").show();
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog createDialogProgress() {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            try {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_circularloader, null);
                builder.setView(v);
                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public AlertDialog createDialogError(String message, String title) {
        try {
            final Button btn_ok;
            final TextView textTitle, textMessage;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_error, null);

            btn_ok = v.findViewById(R.id.buttonAction);
            textTitle = v.findViewById(R.id.textTitle);
            textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(title);
            textMessage.setText(message);
            btn_ok.setText(getResources().getString(R.string.okay));
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

    private void UpdateImage(){
        RequestParameter parameter = new RequestParameter();
        parameter.user= global.getUser();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        //dialogProgress.show();
        try {
            Call<ResponseData> result = methodWS.setuserImage (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            cimgprofile.setEnabled(true);
                            dialogProgress.dismiss();
                            global.getUser().setBase64String(null);
                            Toast.makeText(context, "Imagen grabada correctamente", Toast.LENGTH_SHORT).show();
                        }else if(request.getCodigoError()==2){
                            dialogProgress.dismiss();
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                        }
                    }else{
                        dialogProgress.dismiss();
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "Error: UpdateImage").show();
        }
    }
}
