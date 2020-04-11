package com.diamante.clubconstructor.response;

import com.diamante.clubconstructor.model.About;
import com.diamante.clubconstructor.model.BonusCanje;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Charlas;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.DatosPerson;
import com.diamante.clubconstructor.model.Direccion;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.Eventos;
import com.diamante.clubconstructor.model.Faqs;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Jobs;
import com.diamante.clubconstructor.model.JobsPublication;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.model.Manual;
import com.diamante.clubconstructor.model.Menu;
import com.diamante.clubconstructor.model.Noticias;
import com.diamante.clubconstructor.model.PromocionCategoria;
import com.diamante.clubconstructor.model.Reward;
import com.diamante.clubconstructor.model.User;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

public class ResponseData implements Serializable {
    public int codigoError;
    public String mensajeUsuario;
    public String mensajeSistema;

    public User user;
    public List<Local> local;
    public List<Menu> menu;
    public List<GeneralSpinner> spinner_general;
    public List<GeneralSpinner> spinner_tipocliente;
    public List<GeneralSpinner> spinner_itemclasificacion;
    public List<GeneralSpinner> estructura_tipo;

    public Estimated estimated;

    public List<Brick> bricks;
    public List<Estimated> estimated_list;
    public List<EstimatedDetail> estimatedDetail;

    public List<Cotizacion> cotizacion_list;

    public Cotizacion cotizacion;
    public About about;
    public List<Faqs> faqs_list;
    public List<PromocionCategoria> promocioncategoria_list;
    public List<Jobs> jobs_list;
    public List<JobsPublication> jobspublication_list;
    public List<Noticias> noticias_list;
    public List<Eventos> eventos_list;
    public List<Charlas> charlas_list;
    public List<Manual> manual_list;
    public List<Reward> reward_list;
    public Direccion direccion;

    public int getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(int codigoError) {
        this.codigoError = codigoError;
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }

    public void setMensajeUsuario(String mensajeUsuario) {
        this.mensajeUsuario = mensajeUsuario;
    }

    public String getMensajeSistema() {
        return mensajeSistema;
    }

    public void setMensajeSistema(String mensajeSistema) {
        this.mensajeSistema = mensajeSistema;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Local> getLocal() {
        return local;
    }

    public void setLocal(List<Local> local) {
        this.local = local;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public List<GeneralSpinner> getSpinner_general() {
        return spinner_general;
    }

    public void setSpinner_general(List<GeneralSpinner> spinner_general) {
        this.spinner_general = spinner_general;
    }

    public List<GeneralSpinner> getSpinner_tipocliente() {
        return spinner_tipocliente;
    }

    public void setSpinner_tipocliente(List<GeneralSpinner> spinner_tipocliente) {
        this.spinner_tipocliente = spinner_tipocliente;
    }

    public List<GeneralSpinner> getSpinner_itemclasificacion() {
        return spinner_itemclasificacion;
    }

    public void setSpinner_itemclasificacion(List<GeneralSpinner> spinner_itemclasificacion) {
        this.spinner_itemclasificacion = spinner_itemclasificacion;
    }

    public List<GeneralSpinner> getEstructura_tipo() {
        return estructura_tipo;
    }

    public void setEstructura_tipo(List<GeneralSpinner> estructura_tipo) {
        this.estructura_tipo = estructura_tipo;
    }

    public Estimated getEstimated() {
        return estimated;
    }

    public void setEstimated(Estimated estimated) {
        this.estimated = estimated;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public List<Estimated> getEstimated_list() {
        return estimated_list;
    }

    public void setEstimated_list(List<Estimated> estimated_list) {
        this.estimated_list = estimated_list;
    }

    public List<EstimatedDetail> getEstimatedDetail() {
        return estimatedDetail;
    }

    public void setEstimatedDetail(List<EstimatedDetail> estimatedDetail) {
        this.estimatedDetail = estimatedDetail;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public List<Cotizacion> getCotizacion_list() {
        return cotizacion_list;
    }

    public void setCotizacion_list(List<Cotizacion> cotizacion_list) {
        this.cotizacion_list = cotizacion_list;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public List<Faqs> getFaqs_list() {
        return faqs_list;
    }

    public void setFaqs_list(List<Faqs> faqs_list) {
        this.faqs_list = faqs_list;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<PromocionCategoria> getPromocioncategoria_list() {
        return promocioncategoria_list;
    }

    public void setPromocioncategoria_list(List<PromocionCategoria> promocioncategoria_list) {
        this.promocioncategoria_list = promocioncategoria_list;
    }

    public List<Jobs> getJobs_list() {
        return jobs_list;
    }

    public void setJobs_list(List<Jobs> jobs_list) {
        this.jobs_list = jobs_list;
    }

    public List<JobsPublication> getJobspublication_list() {
        return jobspublication_list;
    }

    public void setJobspublication_list(List<JobsPublication> jobspublication_list) {
        this.jobspublication_list = jobspublication_list;
    }

    public List<Noticias> getNoticias_list() {
        return noticias_list;
    }

    public void setNoticias_list(List<Noticias> noticias_list) {
        this.noticias_list = noticias_list;
    }

    public List<Eventos> getEventos_list() {
        return eventos_list;
    }

    public void setEventos_list(List<Eventos> eventos_list) {
        this.eventos_list = eventos_list;
    }

    public List<Charlas> getCharlas_list() {
        return charlas_list;
    }

    public void setCharlas_list(List<Charlas> charlas_list) {
        this.charlas_list = charlas_list;
    }

    public List<Manual> getManual_list() {
        return manual_list;
    }

    public void setManual_list(List<Manual> manual_list) {
        this.manual_list = manual_list;
    }

    public List<Reward> getReward_list() {
        return reward_list;
    }

    public void setReward_list(List<Reward> reward_list) {
        this.reward_list = reward_list;
    }
}
