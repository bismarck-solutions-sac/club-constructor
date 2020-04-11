package com.diamante.clubconstructor.network;
import com.diamante.clubconstructor.api.sunat.response;
import com.diamante.clubconstructor.model.Parametros;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.request.RequestParameter;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MethodWS {

    @GET("ruc/{ruc}")
    @Headers("Content-Type: application/json")
    Call<response> getAPIRuc(@Path("ruc") String ruc);

    @POST("Diamante/autenticarUsuario")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getLoginUser(@Body RequestParameter parameter);

    @POST("Diamante/userProfile")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getuserProfile(@Body RequestParameter parameter);

    @POST("Diamante/userPassword")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserPassword(@Body RequestParameter parameter);

    @POST("Diamante/userAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserAdd(@Body RequestParameter parameter);

    @POST("Diamante/userForgot")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserForgot(@Body RequestParameter parameter);

    @POST("Diamante/userEmpresaAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> userEmpresaAdd(@Body RequestParameter parameter);

    @POST("Diamante/userUpdate")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserUpdate(@Body RequestParameter parameter);

    @POST("Diamante/UploadImageProfile")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserImage(@Body RequestParameter parameter);

    @POST("Diamante/localList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getLocalList();

    @POST("Diamante/menuList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getMenuList();

    @POST("Diamante/menuClubList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getMenuClubList(@Body RequestParameter parameter);

    @POST("Diamante/maestrosList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getMaestrosList(@Body RequestParameter parameter);

    @POST("Diamante/general_spinner")
    @Headers("Content-Type: application/json")
    Call<ResponseData> general_spinner(@Body RequestParameter parameter);

    @POST("Diamante/brickList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> mybrickList(@Body RequestParameter parameter);

    @POST("Diamante/calculation_material")
    @Headers("Content-Type: application/json")
    Call<ResponseData> calculation_material(@Body RequestParameter parameter);

    @POST("Diamante/calculation_add")
    @Headers("Content-Type: application/json")
    Call<ResponseData> calculation_add(@Body RequestParameter parameter);

    @POST("Diamante/calculation_list")
    @Headers("Content-Type: application/json")
    Call<ResponseData> calculation_list(@Body RequestParameter parameter);

    @POST("Diamante/brick_type_list")
    @Headers("Content-Type: application/json")
    Call<ResponseData> brick_type_list();

    @POST("Diamante/cotizacion_totales")
    @Headers("Content-Type: application/json")
    Call<ResponseData> cotizacion_totales(@Body RequestParameter parameter);

    @POST("Diamante/cotizacion_add")
    @Headers("Content-Type: application/json")
    Call<ResponseData> cotizacion_add(@Body RequestParameter parameter);

    @POST("Diamante/cotizacion_list")
    @Headers("Content-Type: application/json")
    Call<ResponseData> cotizacion_list(@Body RequestParameter parameter);

    @POST("Diamante/cotizacion_pdf")
    @Headers("Content-Type: application/json")
    Call<ResponseData> cotizacion_pdf(@Body RequestParameter parameter);

    @POST("Diamante/about")
    @Headers("Content-Type: application/json")
    Call<ResponseData> about(@Body RequestParameter parameter);

    @POST("Diamante/faqs")
    @Headers("Content-Type: application/json")
    Call<ResponseData> faqs();

    @POST("Diamante/userBonusSelect")
    @Headers("Content-Type: application/json")
    Call<ResponseData> userBonusSelect(@Body RequestParameter parameter);

    @POST("Diamante/userDireccion")
    @Headers("Content-Type: application/json")
    Call<ResponseData> userDireccion(@Body RequestParameter parameter);

    @POST("Diamante/direccionAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> direccionAdd(@Body RequestParameter parameter);


    @POST("Diamante/ListaParametros")
    @Headers("Content-Type: application/json")
    Call<List<Parametros>> getParametros(@Body RequestParameter parameter);

    @POST("Diamante/categoriaPromocion")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getpromocionCategoria();

    @POST("Diamante/jobsList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getjobsList();

    @POST("Diamante/noticiasList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getnoticiasList();

    @POST("Diamante/eventosList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> geteventosList(@Body RequestParameter parameter);

    @POST("Diamante/eventosAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> eventosAdd(@Body RequestParameter parameter);

    @POST("Diamante/charlasList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getcharlasList();

    @POST("Diamante/manualesList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getmanualesList();

    @POST("Diamante/rewardsList")
    @Headers("Content-Type: application/json")
    Call<ResponseData> getrewardsList();

    @POST("Diamante/rewardsAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> rewardsAdd(@Body RequestParameter parameter);

    @POST("Diamante/messageAdd")
    @Headers("Content-Type: application/json")
    Call<ResponseData> messageAdd(@Body RequestParameter parameter);

    @POST("Diamante/userToken")
    @Headers("Content-Type: application/json")
    Call<ResponseData> setuserToken(@Body RequestParameter parameter);

}