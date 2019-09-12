package com.ljj.dao;

import com.ljj.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IUserDao {
    /**
     * @param user date formate
     * @return 1:success
     *            -1:alread has this user
     * @throws SQLException
     */
    public int register(User user) throws SQLException;

    /*
    if login success,return the id of the user
    return -1 when don han this user
    return -2 when password not match
     */
    public int login(User user);

    public boolean hasUser(String name, Connection con);

    public boolean ban(int id);

    boolean delete(int userId);

    int update(User user);

    ArrayList<String[]> getActivate();

    ArrayList<User> getAll();

    boolean set(int id);

    int getIdByName(String name);
}
