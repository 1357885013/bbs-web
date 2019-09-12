package com.ljj.factory;

import com.ljj.exception.CloseDbResourceError;
import com.ljj.exception.ConnectToDbError;

import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Factory {
    private static String username;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ResourceBundle resource = ResourceBundle.getBundle("db");
        username = resource.getString("user");
        password = resource.getString("password");

    }

    public static Connection getCon() {
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbs",username ,password);
        } catch (SQLException e) {
            throw new ConnectToDbError(e.getMessage()+username+password);
        }
        return con;
    }

    public static void closeAll(ResultSet res, Statement state, Connection con) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                throw new CloseDbResourceError("when close ResultSet:" + e.getMessage());
            }
        }
        try {
            state.close();
        } catch (SQLException e) {
            throw new CloseDbResourceError("when close StateMent:" + e.getMessage());
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new CloseDbResourceError("when close Connection:" + e.getMessage());
        }
    }
}
