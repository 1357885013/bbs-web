package com.ljj.service;

public interface IReplyService {

    public void delete(int postId, int id, boolean state);

    boolean into();

    void listMy();

    void listReplyed();

    void reply(int userId,int blockId,int postId);

    void refresh(int postId);
}
