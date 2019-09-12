package com.ljj.service.impl;

import com.ljj.dao.impl.BlockDao;
import com.ljj.entity.Block;
import com.ljj.service.IBlockService;
import com.ljj.util.Scan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockService extends HttpServlet implements IBlockService {
    public Block block = new Block();
    private BlockDao dao = new BlockDao();
    private Scanner scan = Scan.scan;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        super.service(req, resp);
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
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                if (id == -1 || name.equals(""))
                    message = "数据错误";
                else if (changeName(id, name))
                    message = ("1>>>>>>>>>修改成功<<<<<<<<");
                else
                    message = ("2>>>>>>>>>修改失败，不存在次id或其它原因<<<<<<<<");
                resp.getWriter().print(message);
                break;
            case "add":
                name = req.getParameter("name");
                JSONmessage = "{\"code\":%d,\"message\":\"%s\",\"id\":%d}";
                code = 0;
                resId = -1;
                block.setName(name);
                try {
                    if (name.equals(""))
                        message = "板块名不能为空！";
                    else if (dao.create(block)) {
                        message = ("添加成功");
                        code = 1;

                    } else
                        message = ("添加失败");

                    resp.getWriter().printf(JSONmessage, code, message, block.getId());
                } catch (SQLException e) {
                    message = ("服务器冒烟了");
                }
                break;
            case "delete":
                id = Integer.parseInt(req.getParameter("id"));
                JSONmessage = "{\"code\":%d,\"message\":\"%s\"}";
                if (id==-1)
                    message = "数据错误";
                else if (delete(id)) {
                    message = ("删除成功");
                    code = 1;
                } else
                    message = ("删除失败");
                resp.getWriter().printf(JSONmessage, code, message);
                break;
        }
    }


    @Override
    public ArrayList<Block> listAll() {
        ArrayList<Block> res = dao.getAllBlocks();
        return res;
    }

    @Override
    public boolean into() {
        if (scan.hasNextInt()) {
            block.setId(scan.nextInt());
            String name = dao.getNameById(block.getId());
            if (name != null) {
                block.setName(name);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return dao.delete(id);
    }

    @Override
    public boolean changeName(int id, String name) {
        return dao.changeName(id, name);
    }
}
