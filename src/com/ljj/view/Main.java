//package com.ljj.view;
//
//import com.ljj.service.impl.BlockService;
//import com.ljj.service.impl.PostService;
//import com.ljj.service.impl.ReplyService;
//import com.ljj.service.impl.UserService;
//import com.ljj.util.Scan;
//
//import java.util.Scanner;
//
//public class Main {
//    private static UserService user = new UserService();
//    private static BlockService block = new BlockService();
//    private static PostService post = new PostService();
//    private static ReplyService reply = new ReplyService();
//    private static Scanner scan = Scan.scan;
//    private static int screen = 1;//标识现在在哪个页面。1：main  2：block   3：post  4：reply
//
//    public static void main(String[] args) {
//        System.out.println("欢迎来到 Console 论坛");
//        priMainMenu();
//
//        while (scan.hasNext()) {
//            if (screen == 5) {    //管理用户
//                switch (scan.next()) {
//                    case "l":
//                    case "list":
//                        user.list();
//                        break;
//                    case "a":
//                    case "add":
//                        user.register();
//                        break;
//                    case "ban":
//                        user.ban();
//                        break;
//                    case "s":
//                    case "set":
//                        user.set();
//                        break;
//                    case "d":
//                    case "del":
//                    case "delete":
//                        user.delete();
//                        break;
//                    case "m":
//                    case "menu":
//                    case "h":
//                    case "help":
//                        priUserMenu();
//                        break;
//                    case "re":
//                    case "return":
//                    case "b":
//                    case "back":
//                        screen = 1;
//                        priBlockMenu();
//                        break;
//                    default:
//                        System.out.println("未知命令，如需帮助请输h 或 help，m 或 menu 也行。。。");
//                }
//            } else if (screen == 4) {    //在某一个帖子里    Reply
//                switch (scan.next()) {
//                    case "l":
//                    case "list":
//                        if (post.listOne(post.post.getId()))
//                            reply.refresh(post.post.getId());
//                        break;
//                    case "rep":
//                    case "reply":
//                        reply.reply(user.user.getId(), block.block.getId(), post.post.getId());
//                        break;
//                    case "d":
//                    case "del":
//                    case "delete":
//                        reply.delete(post.post.getId(), user.user.getId(), user.user.state);
//                        break;
//                    case "m":
//                    case "menu":
//                    case "h":
//                    case "help":
//                        priReplyMenu();
//                        break;
//                    case "re":
//                    case "return":
//                    case "b":
//                    case "back":
//                        screen--;
//                        post.listAll();
//                        break;
//                    default:
//                        System.out.println("未知命令，如需帮助请输h 或 help，m 或 menu 也行。。。");
//                }
//            } else if (screen == 3) {    //在某一个版块里    Post
//                switch (scan.next()) {
//                    case "l":
//                    case "list":
//                        post.listAll();
//                        break;
//                    case "lm":
//                    case "listmy":
//                        post.listMy();
//                        break;
//                    case "lr":
//                    case "listreplyed":
//                        post.listReplyed();
//                        break;
//                    case "i":
//                    case "in":
//                    case "into":
//                        if (post.into()) {
//                            screen++;
//                            //priReplyMenu();
//                            if (post.listOne(post.post.getId()))
//                                reply.refresh(post.post.getId());
//                        }
//                        break;
//                    case "c":
//                    case "create":
//                        post.create(user.user.getId(), block.block.getId());
//                        break;
//                    case "d":
//                    case "del":
//                    case "delete":
//                        post.delete(user.user.getId(), user.user.flag);
//                        break;
//                    case "m":
//                    case "menu":
//                    case "h":
//                    case "help":
//                        priPostMenu();
//                        break;
//                    case "r":
//                    case "return":
//                    case "b":
//                    case "back":
//                        screen--;
//                        block.listAll();
//                        break;
//                    default:
//                        System.out.println("未知命令，如需帮助请输h 或 help，m 或 menu 也行。。。");
//                }
//            } else if (screen == 2) { //登录过了     Block
//                switch (scan.next()) {
//                    case "l":
//                    case "list":
//                        block.listAll();
//                        break;
//                    case "i":
//                    case "in":
//                    case "into":
//                        if (block.into()) {
//                            screen++;
//                            //priPostMenu();
//                            post.listAll();
//                        }
//                        break;
//                    case "la":
//                    case "listActivate":
//                        user.getActivate();
//                        break;
//                    case "u":
//                    case "um":
//                    case "usermanager":
//                        if (user.user.flag) {
//                            screen = 5;
//                            user.list();
//                        }
//                        break;
//                    case "c":
//                    case "create":
//                        if (user.getAuthority())
//                            block.create();
//                        break;
//                    case "d":
//                    case "del":
//                    case "delete":
//                        if (user.getAuthority())
//                            block.delete();
//                        break;
//                    case "m":
//                    case "menu":
//                    case "h":
//                    case "help":
//                        priBlockMenu();
//                        break;
//                    case "q":
//                    case "quit":
//                        System.out.println("quiting...");
//                        System.exit(-1);
//                        break;
//                    default:
//                        System.out.println("未知命令，如需帮助请输h 或 help，m 或 menu 也行。。。");
//                }
//
//                //priBlockMenu();
//            } else if (screen == 1) {   //没有登录     Main
//                switch (scan.next()) {
//                    case "r":
//                    case "register":
//                        user.register();
//                        break;
//                    case "l":
//                    case "login":
//                        if (user.login()) {
//                            screen++;
//                            //priBlockMenu();
//                            block.listAll();
//                        }
//                        break;
//                    case "q":
//                    case "quit":
//                        System.out.println("quiting...");
//                        System.exit(-1);
//                        break;
//                    case "m":
//                    case "menu":
//                    case "h":
//                    case "help":
//                        priMainMenu();
//                        break;
//                    default:
//                        System.out.println("未知命令，如需帮助请输h 或 help，m 或 menu 也行。。。");
//                }
//            } else {
//                System.out.println("Screen ERROR");
//            }
//
//            System.out.println("----------------------------------------------");
//        }
//    }
//
//    private static void priMainMenu() {
//        System.out.println("??????????????????????????????????????????");
//        System.out.println("r|register : 注册");
//        System.out.println("l|login : 登录");
//        System.out.println("h|help|m|menu : 显示功能");
//        System.out.println("q|quit : 退出");
//        System.out.println("l liu 123 i 9 i 9");
//        System.out.println("??????????????????????????????????????????");
//    }
//
//    private static void priBlockMenu() {
//        System.out.println("??????????????????????????????????????????");
//        System.out.println("l|list : 显示所有版块");
//        System.out.println("i|in|into : 进入版块");
//        System.out.println("la||listActitve : 显示活跃用户");
//        if (user.getAuthority()) {
//            System.out.println("c|create : 创建版块");
//            System.out.println("d|del|delete : 删除版块");
//            System.out.println("d|del|delete : 管理用户");
//        }
//        System.out.println("h|help|m|menu : 显示功能");
//        System.out.println("q|quit : 退出");
//        System.out.println("??????????????????????????????????????????");
//    }
//
//    private static void priPostMenu() {
//        System.out.println("??????????????????????????????????????????");
//        System.out.println("l|list : 显示所有帖子");
//        System.out.println("lm|listmy : 我发布的帖子");
//        System.out.println("lr|listreplayed : 我回复过的的帖子");
//        System.out.println("i|in|into : 进入帖子");
//        System.out.println("c|create : 发布帖子");
//        System.out.println("d|del|delete : 删除帖子");
//
//        System.out.println("h|help|m|menu : 显示功能");
//        System.out.println("r|return|b|back : 返回");
//        System.out.println("??????????????????????????????????????????");
//    }
//
//    private static void priReplyMenu() {
//        System.out.println("??????????????????????????????????????????");
//        System.out.println("l|list : 显示帖子所有内容");
//        System.out.println("rep|replay : 回复");
//        System.out.println("d|del|delete : 删除回复");
//
//        System.out.println("h|help|m|menu : 显示功能");
//        System.out.println("re|return|b|back : 返回");
//        System.out.println("??????????????????????????????????????????");
//    }
//
//    private static void priUserMenu() {
//        System.out.println("??????????????????????????????????????????");
//        System.out.println("l|list : 显示所有用户");
//        System.out.println("a|add : 添加用户");
//        System.out.println("d|del|delete : 删除用户");
//        System.out.println("ban : 禁用/启用用户");
//        System.out.println("s|set : 设为管理员/用户");
//
//
//        System.out.println("h|help|m|menu : 显示功能");
//        System.out.println("r|return|b|back : 返回");
//        System.out.println("??????????????????????????????????????????");
//    }
//}
