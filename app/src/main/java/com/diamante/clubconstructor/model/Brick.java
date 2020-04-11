package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class Brick implements Serializable {
    public String brick;
    public String descripcionlocal;
    public double weight;
    public double large;
    public double width;
    public double heigth;
    public String path;
    public String file;
    public double hueco;
    public double performance;
    public boolean selected;
    public double cantidad;
    public String unidadcodigo;
    public String igvexoneradoflag;
    public String brick_type;

    public String getBrick() {
        return brick;
    }

    public void setBrick(String brick) {
        this.brick = brick;
    }

    public String getDescripcionlocal() {
        return descripcionlocal;
    }

    public void setDescripcionlocal(String descripcionlocal) {
        this.descripcionlocal = descripcionlocal;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLarge() {
        return large;
    }

    public void setLarge(double large) {
        this.large = large;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeigth() {
        return heigth;
    }

    public void setHeigth(double heigth) {
        this.heigth = heigth;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public double getHueco() {
        return hueco;
    }

    public void setHueco(double hueco) {
        this.hueco = hueco;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadcodigo() {
        return unidadcodigo;
    }

    public void setUnidadcodigo(String unidadcodigo) {
        this.unidadcodigo = unidadcodigo;
    }

    public String getIgvexoneradoflag() {
        return igvexoneradoflag;
    }

    public void setIgvexoneradoflag(String igvexoneradoflag) {
        this.igvexoneradoflag = igvexoneradoflag;
    }

    public String getBrick_type() {
        return brick_type;
    }

    public void setBrick_type(String brick_type) {
        this.brick_type = brick_type;
    }
}
