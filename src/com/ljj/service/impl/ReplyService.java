package com.ljj.service.impl;

import com.ljj.dao.impl.ReplyDao;
import com.ljj.entity.Reply;
import com.ljj.service.IReplyService;
import com.ljj.util.Scan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReplyService extends HttpServlet implements IReplyService {
    private Scanner scan = Scan.scan;
    private Reply reply = new Reply();
    private ReplyDao dao = new ReplyDao();

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

                if (dao.change(Integer.parseInt(req.getParameter("id")), Integer.parseInt(req.getParameter("postId")), 1, req.getParameter("content")))
                    resp.getWriter().printf(JSONmessage, 1, "修改成功");
                else
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
                    else if (dao.add(reply)) {
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
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                if (id == -1)
                    message = "数据错误";
                else if (dao.delete(id, postId, 1, true)) {
                    message = ("删除成功");
                    code = 1;
                } else
                    message = ("删除失败");
                resp.getWriter().printf(JSONmessage, code, message);
                break;
        }
    }

    @Override
    public void refresh(int postId) {
        ArrayList<Reply> res = dao.getAll(postId);
        Object[] blocks = res.toArray();
        for (Object o : blocks) {
            Reply reply = (Reply) o;
            System.out.println(reply.getId() + "\t\t\t\t" + reply.getContext() + "\t\t\t\t" + reply.getFormatTime());
        }
        System.out.println("~~~~~~~~~~~~~~~~帖子结束~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void reply(int userId, int blockId, int postId) {
        String temp = scan.nextLine();
        if (!temp.equals(""))
            reply.setContext(temp);
        else {
            System.out.println("please input content:");
            if (scan.hasNextLine())
                reply.setContext(scan.nextLine());
        }

        reply.setUserId(userId);
        reply.setPostId(postId);
        reply.setTime(0);
        try {
            if (dao.add(reply)) {
                System.out.println(">>>>>>>>>创建成功<<<<<<<<");
            } else {
                System.out.println(">>>>>>>>>创建失败<<<<<<<<");
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState() + "   " + e.getMessage());
            e.printStackTrace();
            System.out.println(">>>>>>>>>创建失败，已存在同名论坛或其它原因<<<<<<<<");
        }
    }

    @Override
    public void delete(int postId, int userId, boolean state) {
        System.out.println("请输入 id");
        if (scan.hasNextInt()) {
            int id = scan.nextInt();
            if (!state) {
                if (userId != dao.getUserId(id)) {
                    System.out.println("失败，权限不足，你只能删除自己的内容");
                    return;
                }
            }
            if (dao.delete(id, postId, userId, state))
                System.out.println("删除成功");
            else
                System.out.println("删除失败，不存在此id或其它原因");
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
