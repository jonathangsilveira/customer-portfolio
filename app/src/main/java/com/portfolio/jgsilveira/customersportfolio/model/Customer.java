package com.portfolio.jgsilveira.customersportfolio.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.portfolio.jgsilveira.customersportfolio.util.Converters;

import java.util.Date;

@Entity(indices = {@Index(value = {"document"}, unique = true)})
@TypeConverters(Converters.class)
public class Customer {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String document;

    private String name;

    private String documentoId;

    @ColumnInfo(name = "register_date")
    private Date registerDate;

    @ColumnInfo(name = "birthdate")
    private Date birthdate;

    private String state;

    private String telephone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(String documentoId) {
        this.documentoId = documentoId;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
