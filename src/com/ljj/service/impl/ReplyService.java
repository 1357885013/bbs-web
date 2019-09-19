package com.ljj.service.impl;

import com.ljj.dao.IReplyDao;
import com.ljj.entity.Reply;
import com.ljj.factory.MySqlSessionFactory;
import com.ljj.service.IReplyService;
import com.ljj.util.Scan;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class ReplyService extends HttpServlet implements IReplyService {
    private Scanner scan = Scan.scan;
    private Reply reply = new Reply();
    private SqlSession sql = MySqlSessionFactory.getSqlSession();
    private IReplyDao dao = sql.getMapper(IReplyDao.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("postId", req.getParameter("postId"));
        req.setAttribute("blockId", req.getParameter("blockId"));
        req.getRequestDispatcher("reply.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String JSONmessage = "";
        String message = "";
        int resId = -1;
        int code = 0;
        switch (type) {
            case "change":
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                reply.setId(Integer.parseInt(req.getParameter("id")));
                reply.setContext(req.getParameter("content"));
                reply.setPostId(Integer.parseInt(req.getParameter("postId")));
                if (dao.change(reply)) {
                    resp.getWriter().printf(JSONmessage, 1, "修改成功");
                    sql.commit();
                } else
                    resp.getWriter().printf(JSONmessage, 0, "修改失败");
                break;
            case "add":
                String content = req.getParameter("content");
                int postId = Integer.parseInt(req.getParameter("postId"));
                JSONmessage = "{\"code\":%d,\"message\":\"%s\",\"id\":%d}";

                reply.setContext(content);
                reply.setUserId(1);
                reply.setPostId(postId);
                reply.setTime(Long.parseLong(req.getParameter("time")));
                //resp.getWriter().print(reply.toString());
                try {
                    if (content.equals("") || content.equals(""))
                        resp.getWriter().printf(JSONmessage, 0, "容不能为空", -1);
                    else if (dao.add(reply) > 0) {
                        sql.commit();
                        resp.getWriter().printf(JSONmessage, 1, "添加成功", reply.getId());
                    } else
                        resp.getWriter().printf(JSONmessage, 0, "添加失败", -1);

                } catch (SQLException e) {
                    message = ("服务器冒烟了");
                }
                break;
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                postId = Integer.parseInt(req.getParameter("postId"));
                reply.setPostId(postId);
                reply.setId(id);
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                if (id == -1)
                    message = "数据错误";
                else if (dao.delete(reply)) {
                    sql.commit();
                    message = ("删除成功");
                    code = 1;
                } else
                    message = ("删除失败");
                resp.getWriter().printf(JSONmessage, code, message);
                break;
        }
    }

    @Override
    public boolean into() {
        return false;
    }

    @Override
    public void listMy() {

    }

    @Override
    public void listReplyed() {

    }
}
