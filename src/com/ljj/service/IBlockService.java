package com.ljj.service;

import com.ljj.entity.Block;

import java.util.List;

public interface IBlockService {
    List<Block> listAll();

    //public boolean create(String name);

    public boolean delete(int id);
    public boolean changeName(int id,String name);

    boolean into();
}
