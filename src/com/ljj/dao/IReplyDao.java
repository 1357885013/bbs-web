package com.ljj.dao;

import com.ljj.entity.Post;
import com.ljj.entity.Reply;

import java.sql.SQLException;

public interface IReplyDao {
    public Post getAll(int postId);

    int add(Reply reply) throws SQLException;

    public int getIdByTitle(String name);

    public boolean delete(Reply reply);

    public boolean change(Reply reply);

    int getUserId(int id);
}
