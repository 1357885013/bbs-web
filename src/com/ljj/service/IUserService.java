package com.ljj.service;

import com.ljj.entity.User;

import java.util.ArrayList;

public interface IUserService {
    public void register();

    public int login(String username,String password);

    public void ban();

    public boolean getAuthority();

    public boolean delete(int id);

    boolean update();

    void getActivate();

    ArrayList<User> list();

    void set();
}