package com.example.hoahoc.model;

import java.io.Serializable;

public class ExamHistory implements Serializable {
    private int historyId;
    private int examId;
    private String completionTime;
    private int totalCorrect;
    private double score;
    private String questionIds;
    private String userAnswers;

    public ExamHistory(int historyId, int examId, String completionTime, int totalCorrect, double score, String questionIds, String userAnswers) {
        this.historyId = historyId;
        this.examId = examId;
        this.completionTime = completionTime;
        this.totalCorrect = totalCorrect;
        this.score = score;
        this.questionIds = questionIds;
        this.userAnswers = userAnswers;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    public String getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(String userAnswers) {
        this.userAnswers = userAnswers;
    }
}