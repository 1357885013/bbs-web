package com.ljj.entity;

import com.ljj.util.MyTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private int id;
    private String title;
    private String context;
    private long Time;
    private int userId;
    private String userName;
    private int blockId;
    private List<Reply> replies=new ArrayList<>();


    public Post() {
    }

    public Post(int id, String title, String context, int userId, Timestamp time) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.userId = userId;
        Time = time.getTime();
    }

    public Post(int id, String title, String context, int userId, String userName, Timestamp time) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.userId = userId;
        this.userName = userName;
        Time = time.getTime();
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

    public void setTime(Timestamp time) {
        Time = time.getTime();
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

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Reply replies) {
        this.replies.add(replies);
    }
}
