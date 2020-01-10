package com.jishe.jupyter;

import com.jishe.jupyter.entity.WechatUser;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.jishe.jupyter.component.JWT;

/**
 * @program: jupyter
 * @description: 测试jwt加密token的情况
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-11 00:18
 **/
@SpringBootTest
public class jwt_test {
    @Test
    public void contextLoads() throws Exception {
        JWT util = new JWT();
        WechatUser user1 = new WechatUser();
        user1.setOpenId("vjkksdjvkdnjkvnd");
        user1.setNickname("uwdivb");
        user1.setId(22);
        String ab = util.createJWT(6000000, user1);
        System.out.println(ab);
        //eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJEU1NGQVdEV0FEQVMuLi4iLCJzdWIiOiJ7aWQ6MTAwLG5hbWU6eGlhb2hvbmd9IiwidXNlcl9uYW1lIjoiYWRtaW4iLCJuaWNrX25hbWUiOiJEQVNEQTEyMSIsImV4cCI6MTUxNzgzNTE0NiwiaWF0IjoxNTE3ODM1MDg2LCJqdGkiOiJqd3QifQ.ncVrqdXeiCfrB9v6BulDRWUDDdROB7f-_Hg5N0po980
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2amtrc2Rqdmtkbmprdm5kIiwiaWQiOjIyLCJleHAiOjE1Nzg2ODAwMDUsIk9wZW5JZCI6InZqa2tzZGp2a2Ruamt2bmQiLCJpYXQiOjE1Nzg2NzQwMDUsImp0aSI6IjIyIiwiTmlja25hbWUiOiJ1d2RpdmIifQ.KCSX_i3ytk579lLrqXiMoWnwOgkJErS2SbhRt2XwXtM";
        Claims c = util.parseJWT(jwt);//注意：如果jwt已经过期了，这里会抛出jwt过期异常。
        System.out.println(c.getId());//jwt
        System.out.println(c.getIssuedAt());//Mon Feb 05 20:50:49 CST 2018
        System.out.println(c.getSubject());//{id:100,name:xiaohong}
        System.out.println(c.getIssuer());//null
        System.out.println(c.get("uid", String.class));//DSSFAWDWADAS...


    }
}
