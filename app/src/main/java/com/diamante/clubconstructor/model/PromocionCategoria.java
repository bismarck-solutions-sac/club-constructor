package com.diamante.clubconstructor.model;

import java.io.Serializable;
import java.util.List;

public class PromocionCategoria implements Serializable {
    public int id;
    public String name;
    public String color_text;
    public String status;
    public String created_at;
    public String updated_at;
    public List<Promocion> promocion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor_text() {
        return color_text;
    }

    public void setColor_text(String color_text) {
        this.color_text = color_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<Promocion> getPromocion() {
        return promocion;
    }

    public void setPromocion(List<Promocion> promocion) {
        this.promocion = promocion;
    }
}
