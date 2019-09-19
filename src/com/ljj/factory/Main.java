package com.ljj.factory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

public class Main {
    @Test
    public void test1() {
        SqlSessionFactory sql;
        try {
            sql = new SqlSessionFactoryBuilder().build(new FileReader("./src/sqlMapConfig.xml"));
            SqlSession sqlSession = sql.openSession();

            System.out.println("--------------insert---------------");
            MyNew insertNew = new MyNew("insert", "123", "testInsert", new Date());
            System.out.println(sqlSession.insert("insert", insertNew));
            sqlSession.commit();
            System.out.println("--------------getAll---------------");

            List<MyNew> res = sqlSession.selectList("getAll");
            res.forEach(System.out::println);
            System.out.println("--------------getOneById---------------");

            System.out.println((sqlSession.selectOne("getOneById", 19)).toString());
            System.out.println("--------------getAllByLikeCase---------------");

            (sqlSession.selectList("getAllByLikeCase", "i")).forEach(System.out::println);
            System.out.println("--------------update---------------");


            MyNew updateNew = new MyNew("update", "123", "testUpdate", new Date());
            updateNew.setId(19);
            System.out.println(sqlSession.update("update", updateNew));
            sqlSession.commit();

            System.out.println("--------------delete---------------");

            System.out.println(sqlSession.delete("delete", "insert"));
            sqlSession.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        SqlSessionFactory sql;

        SqlSession sqlSession=MySqlSessionFactory.getSqlSession();
        MyDao mapper;
        mapper = sqlSession.getMapper(MyDao.class);


        System.out.println("--------------insert---------------");
        MyNew insertNew = new MyNew("insert", "123", "testInsert", new Date());
        System.out.println(mapper.insert(insertNew));
        System.out.println("insertNew.getId() = " + insertNew.getId());
        sqlSession.commit();
        System.out.println("--------------getAll---------------");

        List<MyNew> res = mapper.getAll();
        res.forEach(System.out::println);
        System.out.println("--------------getOneById---------------");

        System.out.println((mapper.getOneById(19)).toString());
        System.out.println("--------------getAllByLikeCase---------------");

        (mapper.getAllByLikeCase("i")).forEach(System.out::println);
        System.out.println("--------------update---------------");


        MyNew updateNew = new MyNew("update", "123", "testUpdate", new Date());
        updateNew.setId(19);
        System.out.println(mapper.update(updateNew));
        sqlSession.commit();

        System.out.println("--------------delete---------------");

        System.out.println(mapper.delete("insert"));
        sqlSession.commit();
    }
}
