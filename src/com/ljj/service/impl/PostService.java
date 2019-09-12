package com.ljj.service.impl;

import com.ljj.dao.impl.PostDao;
import com.ljj.entity.Post;
import com.ljj.service.IPostService;
import com.ljj.util.Scan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class PostService extends HttpServlet implements IPostService {
    public Post post = new Post();
    private PostDao dao = new PostDao();
    private Scanner scan = Scan.scan;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("blockId", req.getParameter("blockId"));
        req.getRequestDispatcher("Post.jsp").forward(req, resp);
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
                post.setId(Integer.parseInt(req.getParameter("id")));
                post.setTitle(req.getParameter("title"));
                post.setContext(req.getParameter("content"));

                if (dao.change(post))
                    resp.getWriter().printf(JSONmessage, 1, "修改成功");
                else
                    resp.getWriter().printf(JSONmessage, 0, "修改失败");
                break;
            case "add":
                String title = req.getParameter("title");
                String content = req.getParameter("content");
                int blockId = Integer.parseInt(req.getParameter("blockId"));
                JSONmessage = "{\"code\":%d,\"message\":\"%s\",\"id\":%d}";

                post.setTitle(title);
                post.setContext(content);
                post.setUserId(1);
                post.setBlockId(blockId);
                post.setTime(Long.parseLong(req.getParameter("time")));
                try {
                    if (title.equals("") || content.equals(""))
                        resp.getWriter().printf(JSONmessage, 0, "标题和内容不能为空", -1);
                    else if (dao.create(post)) {
                        resp.getWriter().printf(JSONmessage, 1, "添加成功", post.getId());
                    } else
                        resp.getWriter().printf(JSONmessage, 0, "添加失败", -1);

                } catch (SQLException e) {
                    message = ("服务器冒烟了");
                }
                break;
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                if (id==-1)
                    message = "数据错误";
                else if (dao.delete(id)) {
                    message = ("删除成功");
                    code = 1;
                } else
                    message = ("删除失败");
                resp.getWriter().printf(JSONmessage, code, message);
                break;
        }
    }

    @Override
    public ArrayList<Post> listAll() {
        return dao.getAll();
    }

    @Override
    public ArrayList<Post> listByBlockId(int id) {
       return dao.getByBlockId(id);
    }

    @Override
    public boolean listOne(int id) {
        Post p = new Post();
        p.setId(id);
        if (dao.getPostById(p)) {
            System.out.println("~~~~~~~~~~~~~~~~帖子标题~~~~~~~~~~~~~~~~~~~~");
            System.out.println("              《" + p.getTitle() + "》");
            System.out.println("~~~~~~~~~~~~~~~~~~内容~~~~~~~~~~~~~~~~~~~~");
            System.out.println(p.getContext());
            System.out.println("~~~~~~~~~~~~~~~~~~回复~~~~~~~~~~~~~~~~~~~~");
            return true;
        }
        return false;
    }


    @Override
    public boolean into() {
        if (scan.hasNextInt()) {
            post.setId(scan.nextInt());
            if (dao.getPostById(post)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void listMy() {

    }


    @Override
    public void create(int userId, int blockId) {
        String temp = scan.nextLine();
        if (!temp.trim().equals(""))
            post.setTitle(temp);
        else {
            System.out.println("please input a title:");
            if (scan.hasNextLine())
                post.setTitle(scan.nextLine());
        }

        System.out.println("please input content:");
        if (scan.hasNextLine())
            post.setContext(scan.nextLine());
        post.setUserId(userId);
        post.setBlockId(blockId);
        post.setTime(0);
        try {
            if (dao.create(post)) {
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
    public void delete(int userId, boolean state) {
        System.out.println("请输入帖子id:");
        if (scan.hasNextInt()) {
            int id = scan.nextInt();
            if (!state) {
                if (userId != dao.getUserId(id)) {
                    System.out.println("失败，权限不足，你只能删除自己的内容");
                    return;
                }
            }
            if (dao.delete((id)))
                System.out.println(">>>>>>>>>删除成功<<<<<<<<");
            else
                System.out.println(">>>>>>>>>删除失败，不存在次id或其它原因<<<<<<<<");
        }
    }


}
