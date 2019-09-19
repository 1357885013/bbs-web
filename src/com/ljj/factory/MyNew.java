package com.ljj.factory;

import java.util.Date;

public class MyNew {
    private int id;
    private String title;
    private String content;
    private String authu;
    private Date time;

    public MyNew() {
    }

    public MyNew(String title, String content, String authu, Date datetime) {
        this.title = title;
        this.content = content;
        this.authu = authu;
        this.time = datetime;
    }

    @Override
    public String toString() {
        return "MyNew{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authu='" + authu + '\'' +
                ", datetime=" + time +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthu() {
        return authu;
    }

    public void setAuthu(String authu) {
        this.authu = authu;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date datetime) {
        this.time = datetime;
    }
}
