package com.ljj.dao.impl;

import com.ljj.dao.IUserDao;
import com.ljj.entity.User;
import com.ljj.factory.Factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao implements IUserDao {

    @Override
    public int register(User user) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        if (hasUser(user.getName(), con))
            return -1;
        try {
            state = con.prepareStatement("insert into user values(null,?,?,?,?)");
            state.setString(1, user.getName());
            state.setString(2, user.getPassword());
            state.setBoolean(3, user.state);
            state.setBoolean(4, user.flag);

            if (state.executeUpdate() >= 1) {
                int id = this.getIdByName(user.getName());
                if (id == -1) {
                    System.exit(15);
                } else user.setId(id);
                return 1;
            } else
                return 0;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(null, state, con);
        }
        return 0;
    }

    @Override
    public int login(User user) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        if (!hasUser(user.getName(), con))
            return -1;
        //TODO 判断是否被禁用
        try {
            state = con.prepareStatement("select uid,state,flag from user where uName=? and uPass = ?");
            state.setString(1, user.getName());
            state.setString(2, user.getPassword());

            res = state.executeQuery();
            if (res.next()) {
                user.setId(res.getInt(1));
                user.state = (res.getBoolean(2));
                user.flag = (res.getBoolean(3));
                return 1;
            } else
                return -2;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return 0;
    }

    @Override
    public boolean hasUser(String name, Connection con) {
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select * from user where uName = ?");
            state.setString(1, name);
            res = state.executeQuery();

            return res.next();
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean ban(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("UPDATE USER \n" +
                    "\tSET state = state^1 \n" +
                    "WHERE\n" +
                    "\tuid =?");
            state.setInt(1, id);

            return (state.executeUpdate() >= 1);

        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(null, state, con);
        }
        return false;
    }

    @Override
    public boolean delete(int userId) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("delete from user where uId=?");
            state.setInt(1, userId);

            return (state.executeUpdate() >= 1);

        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(null, state, con);
        }
        return false;
    }

    @Override
    public int update(User user) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            if (user.getPassword() == null) {
                state = con.prepareStatement("update user set uName=?,state=?,flag=? where uId=?");
                state.setString(1, user.getName());
                state.setBoolean(2, user.state);
                state.setBoolean(3, user.flag);
                state.setInt(4, user.getId());
            } else {
                state = con.prepareStatement("update user set uName=?,uPass=?,state=?,flag=? where uId=?");
                state.setString(1, user.getName());
                state.setString(2, user.getPassword());
                state.setBoolean(3, user.state);
                state.setBoolean(4, user.flag);
                state.setInt(5, user.getId());
            }

            return (state.executeUpdate() >= 1) ? 1 : 0;

        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(null, state, con);
        }
        return 0;
    }

    @Override
    public ArrayList<String[]> getActivate() {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<String[]> names = new ArrayList<>();
        try {
            state = con.prepareStatement("SELECT\n" +
                    "\tuName,\n" +
                    "\tsum( num ) num \n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t( SELECT uId, count( * ) num FROM post GROUP BY uId ) UNION ALL\n" +
                    "\t( SELECT uId, count( * ) num FROM reply GROUP BY uId ) \n" +
                    "\t) a\n" +
                    "\tJOIN USER USING ( uId ) \n" +
                    "GROUP BY\n" +
                    "\tuId \n" +
                    "ORDER BY\n" +
                    "\tnum DESC;");
            res = state.executeQuery();

            while (res.next()) {
                String[] struct = new String[2];
                struct[0] = res.getString(1);
                struct[1] = new Integer(res.getInt(2)).toString();
                names.add(struct);
            }
            return names;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            state = con.prepareStatement("select uId,uName,state,flag from user");
            res = state.executeQuery();

            while (res.next()) {
                users.add(new User(res.getInt(1), res.getString(2), res.getBoolean(3), res.getBoolean(4)));
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return null;
    }

    @Override
    public boolean set(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("UPDATE USER \n" +
                    "\tSET flag = flag^1 \n" +
                    "WHERE\n" +
                    "\tuid =?");
            state.setInt(1, id);

            return (state.executeUpdate() >= 1);

        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(null, state, con);
        }
        return false;
    }

    @Override
    public int getIdByName(String name) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            state = con.prepareStatement("select uId from user where uName=?");
            state.setString(1, name);
            res = state.executeQuery();

            if (res.next()) {
                return res.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return -1;
    }
}
