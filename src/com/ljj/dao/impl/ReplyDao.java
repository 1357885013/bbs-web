package com.ljj.dao.impl;

import com.ljj.entity.Reply;
import com.ljj.factory.Factory;

import java.sql.*;
import java.util.ArrayList;

public class ReplyDao {

    public ArrayList<Reply> getAll(int postId) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<Reply> posts = new ArrayList<>();
        try {
            state = con.prepareStatement("select rId,rContext,uName,uId,rTime from reply join user using (uid) where pId=? order by rTime");
            state.setInt(1, postId);
            res = state.executeQuery();
            while (res.next()) {
                posts.add(new Reply(res.getInt(1), res.getString(2), res.getString(3), res.getInt(4), res.getTimestamp(5)));
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

//    public boolean add(Reply reply) throws SQLException {
//        Connection con = Factory.getCon();
//        PreparedStatement state = null;
//        try {
//            state = con.prepareStatement("insert into reply values(null,?,?,?,?)");
//            state.setString(1, reply.getContext());
//            state.setTimestamp(2, new Timestamp(reply.getTime()));
//            state.setInt(3, reply.getUserId());
//            state.setInt(4, reply.getPostId());
//
//            return state.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return false;
//        } finally {
//            Factory.closeAll(null, state, con);
//        }
//    }


    public int getIdByTitle(String title) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select rId from reply where rTitle=?");
            state.setString(1, title);
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

    public boolean delete(int replyId, int postId, int userId, boolean isAdmin) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("delete from reply where pId=? and rId=?");
            state.setInt(1, postId);
            state.setInt(2, replyId);

            int res = state.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
        } finally {
            Factory.closeAll(null, state, con);
        }
        return false;
    }

    public boolean change(int replyId, int postId, int userId, String content) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("update reply set rContext=? where pId=? and rId=?");
            state.setString(1, content);
            state.setInt(2, postId);
            state.setInt(3, replyId);

            int res = state.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
        } finally {
            Factory.closeAll(null, state, con);
        }
        return false;
    }


    public int getUserId(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select uId from reply where rId=?");
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
