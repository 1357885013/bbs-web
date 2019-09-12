package com.ljj.service;

import com.ljj.entity.Post;

import java.util.ArrayList;

public interface IPostService {
    ArrayList<Post> listAll();

    public void create(int userId,int blockId);

    public void delete(int userId, boolean state);

    boolean into();

    void listMy();
    boolean listOne(int id);

    public ArrayList<Post> listByBlockId(int id);
}
