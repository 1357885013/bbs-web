package com.ljj.dao.impl;

import com.ljj.dao.IPostDao;
import com.ljj.entity.Post;
import com.ljj.factory.Factory;

import java.sql.*;
import java.util.ArrayList;

public class PostDao implements IPostDao {
    @Override
    public ArrayList<Post> getAll() {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<Post> posts = new ArrayList<>();
        try {
            state = con.prepareStatement("select pId,pTitle,pContext,uId,pTime from post");
            res = state.executeQuery();
            while (res.next()) {
                posts.add(new Post(res.getInt(1), res.getString(2), res.getString(3), res.getInt(4), res.getTimestamp(5)));
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return posts;
    }
    @Override
    public ArrayList<Post> getByBlockId(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<Post> posts = new ArrayList<>();
        try {
            state = con.prepareStatement("select pId,pTitle,pContext,uId,uName,pTime from post join user using(uId) where bId = ?");
            state.setInt(1,id);
            res = state.executeQuery();
            while (res.next()) {
                posts.add(new Post(res.getInt(1), res.getString(2), res.getString(3), res.getInt(4),res.getString(5), res.getTimestamp(6)));
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return posts;
    }
    @Override
    public boolean create(Post post) throws SQLException {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("insert into post values(null,?,?,?,?,?)");
            state.setString(1, post.getTitle());
            state.setString(2, post.getContext());
            state.setTimestamp(3, new Timestamp(post.getTime()));
            state.setInt(4, post.getUserId());
            state.setInt(5, post.getBlockId());

            if (state.executeUpdate() > 0) {
                int id = getIdByTitle(post.getTitle());
                if (id != -1) {
                    post.setId(id);
                    return true;
                }
            }
            return false;
        }catch(SQLException e){
            return false;
        } finally {
            Factory.closeAll(null, state, con);
        }
    }

    private int getIdByTitle(String title) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select pId from post where pTitle=?");
            state.setString(1, title);
            res = state.executeQuery();
            if (res.next()) {
                return res.getInt(1);
            } else
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
    @Override
    public boolean change(Post post){
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("update post set pTitle=?,pContext=? where pId=?");
            state.setString(1,post.getTitle());
            state.setString(2,post.getContext());
            state.setInt(3, post.getId());

            int res = state.executeUpdate();
            return res > 0;
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
    public String getNameById(int id) {
        return null;
    }

    @Override
    public int getIdByName(String name) {
        return 0;
    }

    @Override
    public boolean delete(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("delete from post where pId=?");
            state.setInt(1, id);

            int res = state.executeUpdate();
            return res > 0;
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
    public boolean getPostById(Post post) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select pId,pTitle,pContext,uId,uName,pTime from post join user using(uId) where pId = ?");
            state.setInt(1, post.getId());
            res = state.executeQuery();
            if (res.next()) {
                post.setTitle(res.getString(2));
                post.setContext(res.getString(3));
                post.setUserId(res.getInt(4));
                post.setUserName(res.getString(5));
                post.setTime(res.getTimestamp(6));
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return false;
    }

    @Override
    public int getUserId(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select uId from post where pId=?");
            state.setInt(1, id);
            res = state.executeQuery();
            if (res.next()) {
                return res.getInt(1);
            }
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
