package com.ljj.dao;

import com.ljj.entity.Block;

import java.sql.SQLException;
import java.util.List;

public interface IBlockDao {
    public List<Block> getAllBlocks();

    public boolean create(Block block) throws SQLException;

    public String getNameById(int id);
    public int getIdByName(String name);
    public boolean delete(int id);
    public boolean changeName(int id,String name);
}
