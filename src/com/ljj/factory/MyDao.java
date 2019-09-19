package com.ljj.factory;

import java.util.List;

public interface MyDao {
    public List<MyNew> getAll();
    public MyNew getOneById(int id);
    public List<MyNew> getAllByLikeCase(String value);
    public int insert(MyNew obj);
    public int update(MyNew obj);
    public int delete(String title);
}
