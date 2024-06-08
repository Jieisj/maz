package com.maz.app;

import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pojo.UserHello;
import query.Userquery;

import java.io.IOException;
import java.io.InputStream;

public class RunMapper {
    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resource);
        ){
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            try (SqlSession sqlSession = sqlSessionFactory.openSession()){
                UserMapper<UserHello, Userquery> mapper = sqlSession.getMapper(UserMapper.class);
                Userquery userQuery = new Userquery();
                userQuery.setAge(21);
                Integer i = mapper.selectCount(userQuery);
                System.out.println(i);
                sqlSession.commit();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
