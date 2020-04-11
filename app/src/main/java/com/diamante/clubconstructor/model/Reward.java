package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class Reward implements Serializable {
    public int id;
    public String name;
    public String itemcodigo;
    public String tipodetalle;
    public double value;
    public double stock;
    public double percent_notification;
    public String description;
    public int position;
    public String path;
    public String status;
    public String sendmail;
    public String created_at;
    public String updated_at;

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

    public String getItemcodigo() {
        return itemcodigo;
    }

    public void setItemcodigo(String itemcodigo) {
        this.itemcodigo = itemcodigo;
    }

    public String getTipodetalle() {
        return tipodetalle;
    }

    public void setTipodetalle(String tipodetalle) {
        this.tipodetalle = tipodetalle;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPercent_notification() {
        return percent_notification;
    }

    public void setPercent_notification(double percent_notification) {
        this.percent_notification = percent_notification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendmail() {
        return sendmail;
    }

    public void setSendmail(String sendmail) {
        this.sendmail = sendmail;
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
}
