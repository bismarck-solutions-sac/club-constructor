package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class Local implements Serializable {

    public int id;
    public String admin;
    public String name;
    public String address;
    public String free_phone;
    public String phone;
    public String path;
    public float latitude;
    public float longitude;
    public String city_origen;
    public String status;
    public String sucursal;
    public String almacencodigo;
    public String created_at;
    public String updated_at;
    public String address_2;
    public double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFree_phone() {
        return free_phone;
    }

    public void setFree_phone(String free_phone) {
        this.free_phone = free_phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCity_origen() {
        return city_origen;
    }

    public void setCity_origen(String city_origen) {
        this.city_origen = city_origen;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getAlmacencodigo() {
        return almacencodigo;
    }

    public void setAlmacencodigo(String almacencodigo) {
        this.almacencodigo = almacencodigo;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }
}
