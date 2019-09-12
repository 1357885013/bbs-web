package com.ljj.entity;

import com.ljj.util.MyTime;

import java.sql.Timestamp;
import java.util.Date;

public class Reply {
    private int id;
    private String title;
    private String context;
    private long Time;
    private int userId;
    private String userName;
    private int postId;

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", Time=" + Time +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", postId=" + postId +
                '}';
    }

    public Reply() {
    }

    public Reply(int id, String context, String userName, Timestamp time) {
        this.id = id;
        this.context = context;
        Time = time.getTime();
        this.userName = userName;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getFormatTime() {
        return MyTime.formateTime(Time);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
