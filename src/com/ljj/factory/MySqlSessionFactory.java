package com.ljj.factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class MySqlSessionFactory {
    private MySqlSessionFactory() {
    }

    public static SqlSession getSqlSession() {
        try {
            return new sql().sqlSession;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class sql {
//        public SqlSession sqlSession = (new SqlSessionFactoryBuilder().build(new FileReader("./src/com/ljj/factory/sqlMapConfig.xml"))).openSession();
        public SqlSession sqlSession = (new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("sqlMapConfig.xml"))).openSession();

        public sql() throws IOException {
        }
    }
}
