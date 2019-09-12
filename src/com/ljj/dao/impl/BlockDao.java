package com.ljj.dao.impl;

import com.ljj.dao.IBlockDao;
import com.ljj.entity.Block;
import com.ljj.factory.Factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BlockDao implements IBlockDao {
    @Override
    public ArrayList<Block> getAllBlocks() {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        ArrayList<Block> blocks = new ArrayList<>();
        try {
            state = con.prepareStatement("select * from block");
            res = state.executeQuery();
            while (res.next()) {
                blocks.add(new Block(res.getInt(1), res.getString(2)));
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + e.getSQLState());
            e.printStackTrace();
            System.exit(-1);
        } finally {
            Factory.closeAll(res, state, con);
        }
        return blocks;
    }

    @Override
    public boolean create(Block block) throws SQLException {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("insert into block values(null,?)");
            state.setString(1, block.getName());


            if (state.executeUpdate() > 0) {
                int id = getIdByName(block.getName());
                if (id != -1) {
                    block.setId(id);
                    return true;
                }
            }
            return false;
        } finally {
            Factory.closeAll(null, state, con);
        }
    }

    @Override
    public String getNameById(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select bName from block where bId=?");
            state.setInt(1, id);

            res = state.executeQuery();
            if (res.next()) {
                return res.getString(1);
            } else
                return null;
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
    public int getIdByName(String name) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        ResultSet res = null;
        try {
            state = con.prepareStatement("select bId from block where bName=?");
            state.setString(1, name);

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
    public boolean delete(int id) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("delete from block where bId=?");
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
    public boolean changeName(int id,String name) {
        Connection con = Factory.getCon();
        PreparedStatement state = null;
        try {
            state = con.prepareStatement("update block set bName =? where bId=?");
            state.setString(1, name);
            state.setInt(2, id);

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
}
