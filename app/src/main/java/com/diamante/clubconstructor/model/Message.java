package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class Message implements Serializable {
    public int id;
    public int id_user;
    public int id_category_message;
    public int id_status_messages;
    public String name;
    public String email;
    public String subject;
    public String message;
    public String created_at;
    public String updated_at;
    public String send_email;
    public String send_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_category_message() {
        return id_category_message;
    }

    public void setId_category_message(int id_category_message) {
        this.id_category_message = id_category_message;
    }

    public int getId_status_messages() {
        return id_status_messages;
    }

    public void setId_status_messages(int id_status_messages) {
        this.id_status_messages = id_status_messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getSend_email() {
        return send_email;
    }

    public void setSend_email(String send_email) {
        this.send_email = send_email;
    }

    public String getSend_at() {
        return send_at;
    }

    public void setSend_at(String send_at) {
        this.send_at = send_at;
    }
}
