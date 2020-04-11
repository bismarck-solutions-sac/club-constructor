package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class EstimatedDetail implements Serializable {

    public int id;
    public int estimated_material_id;
    public String brick;
    public String brick_name;
    public String unidadcodigo;
    public String igvexoneradoflag;
    public String type_brick;
    public double total_brick;
    public double area;
    public double bag_cement;
    public int bag_require;
    public double mortar;
    public int mortar_require;
    public double rock;
    public int rock_require;
    public String path;
    public String status;
    public String created_at;
    public String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstimated_material_id() {
        return estimated_material_id;
    }

    public void setEstimated_material_id(int estimated_material_id) {
        this.estimated_material_id = estimated_material_id;
    }

    public String getBrick() {
        return brick;
    }

    public void setBrick(String brick) {
        this.brick = brick;
    }

    public String getBrick_name() {
        return brick_name;
    }

    public void setBrick_name(String brick_name) {
        this.brick_name = brick_name;
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

    public String getType_brick() {
        return type_brick;
    }

    public void setType_brick(String type_brick) {
        this.type_brick = type_brick;
    }

    public double getTotal_brick() {
        return total_brick;
    }

    public void setTotal_brick(double total_brick) {
        this.total_brick = total_brick;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getBag_cement() {
        return bag_cement;
    }

    public void setBag_cement(double bag_cement) {
        this.bag_cement = bag_cement;
    }

    public int getBag_require() {
        return bag_require;
    }

    public void setBag_require(int bag_require) {
        this.bag_require = bag_require;
    }

    public double getMortar() {
        return mortar;
    }

    public void setMortar(double mortar) {
        this.mortar = mortar;
    }

    public int getMortar_require() {
        return mortar_require;
    }

    public void setMortar_require(int mortar_require) {
        this.mortar_require = mortar_require;
    }

    public double getRock() {
        return rock;
    }

    public void setRock(double rock) {
        this.rock = rock;
    }

    public int getRock_require() {
        return rock_require;
    }

    public void setRock_require(int rock_require) {
        this.rock_require = rock_require;
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
