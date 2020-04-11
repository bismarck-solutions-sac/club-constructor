package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class Level implements Serializable {

    public int id;
    public String name;
    public int value_init;
    public int value_end;
    public String path;

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

    public int getValue_init() {
        return value_init;
    }

    public void setValue_init(int value_init) {
        this.value_init = value_init;
    }

    public int getValue_end() {
        return value_end;
    }

    public void setValue_end(int value_end) {
        this.value_end = value_end;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
