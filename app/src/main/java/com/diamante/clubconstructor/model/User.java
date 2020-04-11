package com.diamante.clubconstructor.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public int id;
    public String first_name;
    public String last_name;
    public String dni;
    public String email;
    public String password;
    public String tipocliente;
    public String tipocliente_name;
    public String fechanacimiento;
    public String telefono;
    public String departamento;
    public String departamento_name;
    public String provincia;
    public String provincia_name;
    public String distrito;
    public String distrito_name;
    public String full_name;
    public String path;
    public String profesion;
    public String profesion_name;
    public String idgoogle;
    public String base64String;
    public String token;


    public int level;
    public List<UserEmpresa> userempresa;
    public List<Level> level_list;
    public List<BonusCanje> bonus_canje;
    public List<Direccion> direccion_list;

    public int codigopersona;
    public String escliente;
    public double puntos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipocliente() {
        return tipocliente;
    }

    public void setTipocliente(String tipocliente) {
        this.tipocliente = tipocliente;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDepartamento_name() {
        return departamento_name;
    }

    public void setDepartamento_name(String departamento_name) {
        this.departamento_name = departamento_name;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProvincia_name() {
        return provincia_name;
    }

    public void setProvincia_name(String provincia_name) {
        this.provincia_name = provincia_name;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDistrito_name() {
        return distrito_name;
    }

    public void setDistrito_name(String distrito_name) {
        this.distrito_name = distrito_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<UserEmpresa> getUserempresa() {
        return userempresa;
    }

    public void setUserempresa(List<UserEmpresa> userempresa) {
        this.userempresa = userempresa;
    }

    public int getCodigopersona() {
        return codigopersona;
    }

    public void setCodigopersona(int codigopersona) {
        this.codigopersona = codigopersona;
    }

    public String getEscliente() {
        return escliente;
    }

    public void setEscliente(String escliente) {
        this.escliente = escliente;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public List<Level> getLevel_list() {
        return level_list;
    }

    public void setLevel_list(List<Level> level_list) {
        this.level_list = level_list;
    }

    public List<BonusCanje> getBonus_canje() {
        return bonus_canje;
    }

    public void setBonus_canje(List<BonusCanje> bonus_canje) {
        this.bonus_canje = bonus_canje;
    }

    public List<Direccion> getDireccion_list() {
        return direccion_list;
    }

    public void setDireccion_list(List<Direccion> direccion_list) {
        this.direccion_list = direccion_list;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getProfesion_name() {
        return profesion_name;
    }

    public void setProfesion_name(String profesion_name) {
        this.profesion_name = profesion_name;
    }

    public String getIdgoogle() {
        return idgoogle;
    }

    public void setIdgoogle(String idgoogle) {
        this.idgoogle = idgoogle;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public String getTipocliente_name() {
        return tipocliente_name;
    }

    public void setTipocliente_name(String tipocliente_name) {
        this.tipocliente_name = tipocliente_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
