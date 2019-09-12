package com.ljj.service.impl;

import com.ljj.dao.impl.UserDao;
import com.ljj.entity.User;
import com.ljj.service.IUserService;
import com.ljj.util.Scan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UserService extends HttpServlet implements IUserService {
    public User user = new User();
    private Scanner scan = Scan.scan;
    private UserDao dao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String type = req.getParameter("type");
        if (type == null) type = "login";
        String JSONmessage = "{\"code\":%d,\"message\":\"%s\",\"id\":%d}";

        switch (type) {
            case "add":
                if (username.equals("") || password.equals("")) {
                    resp.getWriter().printf(JSONmessage, 0, "账号和密码不能为空！", -1);
                    return;
                }
                User user = new User(username, password, (req.getParameter("ban") != null), (req.getParameter("admin") != null));
                switch (dao.register(user)) {
                    case 1:
                        resp.getWriter().printf(JSONmessage, 1, "添加成功！", user.getId());
                        break;
                    case 0:
                        resp.getWriter().printf(JSONmessage, 0, "添加失败！", -1);
                        break;
                    case -1:
                        resp.getWriter().printf(JSONmessage, -1, "用户名已被占用！", -1);
                        break;
                }
                break;
            case "login":
                if (username.equals("") || password.equals("")) {
                    resp.getWriter().printf(JSONmessage, 0, "账号和密码不能为空！", -1);
                    return;
                }
                switch (login(username, password)) {
                    case 1:
                        req.setAttribute("message", "login succed,Welcome to LJJ BBS");
                        req.getSession().setAttribute("logined",new Date().getTime());
                        req.getRequestDispatcher("block.jsp").forward(req, resp);
                        break;
                    case 0:
                        req.setAttribute("message", "login failed");
                        req.getRequestDispatcher("login.jsp").forward(req, resp);
                        break;
                    case -1:
                        req.setAttribute("message", "user name error!");
                        req.getRequestDispatcher("login.jsp").forward(req, resp);
                        break;
                    case -2:
                        req.setAttribute("message", "password error!");
                        req.getRequestDispatcher("login.jsp").forward(req, resp);
                        break;
                }
                break;
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                String message = "";
                int code = 0;
                if (id == -1)
                    message = "数据错误";
                else if (delete(id)) {
                    message = ("删除成功");
                    code = 1;
                } else
                    message = ("删除失败");
                resp.getWriter().printf(JSONmessage, code, message);
                break;
            case "change":
                if (username.equals("")) {
                    resp.getWriter().printf(JSONmessage, 0, "账号不能为空！", -1);
                    return;
                }
                user = new User(username, req.getParameter("password"), (req.getParameter("ban") != null), (req.getParameter("admin") != null));
                user.setId(Integer.parseInt(req.getParameter("userId")));
                switch (dao.update(user)) {
                    case 1:
                        resp.getWriter().printf(JSONmessage, 1, "修改成功！", user.getId());
                        break;
                    case 0:
                        resp.getWriter().printf(JSONmessage, 0, "修改失败！", -1);
                        break;
                    case -1:
                        resp.getWriter().printf(JSONmessage, -1, "用户名已被占用！", -1);
                        break;
                }
                break;
        }

    }

    @Override
    public void register() {
        User user = new User();
        System.out.println("please input your name:");
        if (scan.hasNext())
            user.setName(scan.next());
        System.out.println("please input your password:");
        if (scan.hasNext())
            user.setPassword(scan.next());

        switch (dao.register(user)) {
            case 1:
                System.out.println("regist succed");
                break;
            case 0:
                System.out.println("regist failed");
                break;
            case -1:
                System.out.println("this name is already be used!");
                break;
        }
    }

    @Override
    public int login(String username, String password) {
        user.setName(username);
        user.setPassword(password);
        return dao.login(user);
    }

    @Override
    public void ban() {
        System.out.println("输入用户的id");
        if (scan.hasNextInt()) {
            if (dao.ban(scan.nextInt())) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }
        }
    }

    @Override
    public boolean getAuthority() {
        return user.flag;
    }

    @Override
    public boolean delete(int id) {
        return dao.delete(id);
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void getActivate() {
        ArrayList<String[]> res = dao.getActivate();
        System.out.println("活跃用户------------------");
        for (int i = 0; i < res.size(); i++) {
            String[] temp = res.get(i);
            System.out.println(temp[0] + "\t\t\t" + temp[1]);
        }
        System.out.println("--------------------------");
    }

    @Override
    public ArrayList<User> list() {
        return dao.getAll();
    }

    @Override
    public void set() {
        System.out.println("输入用户的id");
        if (scan.hasNextInt()) {
            if (dao.set(scan.nextInt())) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }
        }
    }
}
