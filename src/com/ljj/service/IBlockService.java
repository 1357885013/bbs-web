package com.ljj.service;

import com.ljj.entity.Block;

import java.util.ArrayList;

public interface IBlockService {
    ArrayList<Block> listAll();

    //public boolean create(String name);

    public boolean delete(int id);
    public boolean changeName(int id,String name);

    boolean into();
}
