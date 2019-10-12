package com.ljj.dao.impl;

import com.ljj.dao.IBlockDao;
import com.ljj.entity.Block;
import com.ljj.factory.Factory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("BlockDao")
public class BlockDao extends JdbcDaoSupport implements IBlockDao {
    @Autowired
    private ComboPooledDataSource dataSource;

    @PostConstruct
    private void init() {
        super.setDataSource(dataSource);
    }

    @Override
    public List<Block> getAllBlocks() {

        return super.getJdbcTemplate().query("select * from block", new RowMapper<Block>() {
            @Override
            public Block mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Block(resultSet.getInt(1), resultSet.getString(2));
            }
        });
    }

    @Override
    public boolean create(Block block) throws SQLException {
        if (getJdbcTemplate().update("insert into block values(null,?)", block.getName()) > 0) {
            int id = getIdByName(block.getName());
            if (id != -1) {
                block.setId(id);
                return true;
            }
        }
        return false;
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
        return getJdbcTemplate().queryForObject("select bId from block where bName=?", Integer.class,name);
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
    public boolean changeName(int id, String name) {
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
