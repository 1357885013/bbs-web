package com.ljj.entity;

public class User {
    public boolean state;//是否被ban，
    public boolean flag;//1为用户。
    private int id;
    private String name;
    private String password;

    public User() {
        this.id=-1;
        this.state = false;
        this.flag = false;
    }

    public User( int id, String name,boolean state, boolean flag) {
        this.state = state;
        this.flag = flag;
        this.id = id;
        this.name = name;
    }

    public User(String name, String password, boolean state, boolean flag) {
        this.id = -1;
        this.name = name;
        this.password = password;
        this.state = state;
        this.flag = flag;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin(){
        return flag;
    }
    public boolean isBaned(){
        return state;
    }
}
