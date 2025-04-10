package com.example.hoahoc.model;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String tenchuong, Lop, thongtin;
    private int id;
    private boolean trangthai;

    public Lesson(String id, String tenchuong, String Lop, String thongtin) {
        this.id = Integer.parseInt(id);
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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isSaved() {
        return trangthai;
    }
    public void setSaved(boolean saved) {
        trangthai = saved;
    }
}