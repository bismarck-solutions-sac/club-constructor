package com.diamante.clubconstructor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Estimated implements Serializable {
    public int id;
    public int user_id;
    public String name;
    public String status;
    public String created_at;
    public String updated_at;
    private List<EstimatedDetail> detail = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<EstimatedDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<EstimatedDetail> detail) {
        this.detail = detail;
    }
}
