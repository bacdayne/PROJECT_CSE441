package com.example.hoahoc.model;

import java.io.Serializable;

public class baigiang implements Serializable {
    private String id, tenchuong, Lop, thongtin;

    public baigiang(String id, String tenchuong, String Lop, String thongtin) {
        this.id = id;
        this.tenchuong = tenchuong;
        this.Lop = Lop;
        this.thongtin = thongtin;

    }

    public String getTenchuong() {
        return tenchuong;
    }

    public void setTenchuong(String tenchuong) {
        this.tenchuong = tenchuong;
    }
    public String getLop() {
        return Lop;
    }
    public void setLop(String Lop) {
        this.Lop = Lop;
    }
    public String getThongtin() {
        return thongtin;
    }
    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
