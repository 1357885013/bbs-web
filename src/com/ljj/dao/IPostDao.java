package com.ljj.dao;

import com.ljj.entity.Post;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IPostDao {
    public ArrayList<Post> getAll();

    ArrayList<Post> getByBlockId(int id);

    public boolean create(Post block) throws SQLException;

    public String getNameById(int id);

    public int getIdByName(String name);

    public boolean delete(int id);

    boolean getPostById(Post post);

    int getUserId(int id);
    public boolean change(Post post);

}
