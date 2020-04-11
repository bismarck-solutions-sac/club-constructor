package com.diamante.clubconstructor.request;

import com.diamante.clubconstructor.model.About;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.Direccion;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.Eventos;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Message;
import com.diamante.clubconstructor.model.Reward;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.model.UserEmpresa;
import com.diamante.clubconstructor.response.MaestrosRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestParameter implements Serializable {
    public User user;
    public GeneralSpinner brick_type;
    public MaestrosRequest maestrosRequest;
    public Brick brick;
    public Estimated estimated;
    public Cotizacion cotizacion;
    public UserEmpresa userEmpresa;
    public Eventos evento;
    public Reward reward;
    public Message message;

    /*para eliminar*/
    public List<EstimatedDetail> estimatedDetail = new ArrayList<>();
    /*para eliminar*/

    public double largo;
    public double alto ;
    public String type_menu;
    public String king_wall;
    public About about;
    public Direccion direccion;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GeneralSpinner getBrick_type() {
        return brick_type;
    }

    public void setBrick_type(GeneralSpinner brick_type) {
        this.brick_type = brick_type;
    }

    public MaestrosRequest getMaestrosRequest() {
        return maestrosRequest;
    }

    public void setMaestrosRequest(MaestrosRequest maestrosRequest) {
        this.maestrosRequest = maestrosRequest;
    }

    public Brick getBrick() {
        return brick;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
    }

    public Estimated getEstimated() {
        return estimated;
    }

    public void setEstimated(Estimated estimated) {
        this.estimated = estimated;
    }

    public List<EstimatedDetail> getEstimatedDetail() {
        return estimatedDetail;
    }

    public void setEstimatedDetail(List<EstimatedDetail> estimatedDetail) {
        this.estimatedDetail = estimatedDetail;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public String getKing_wall() {
        return king_wall;
    }

    public void setKing_wall(String king_wall) {
        this.king_wall = king_wall;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public UserEmpresa getUserEmpresa() {
        return userEmpresa;
    }

    public void setUserEmpresa(UserEmpresa userEmpresa) {
        this.userEmpresa = userEmpresa;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getType_menu() {
        return type_menu;
    }

    public void setType_menu(String type_menu) {
        this.type_menu = type_menu;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
