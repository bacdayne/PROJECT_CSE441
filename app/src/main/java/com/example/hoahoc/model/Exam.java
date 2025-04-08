package com.example.hoahoc.model;

public class Exam {
    private int id;
    private int examNumber;

    public Exam(int id, int examNumber) {
        this.id = id;
        this.examNumber = examNumber;
    }

    public int getId() {
        return id;
    }

    public int getExamNumber() {
        return examNumber;
    }
}
