package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: jupyter
 * @description: 微信用户实体类，基于JPA实现，描述在小程序上使用的用户数据表等。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-08 20:55
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class WechatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Nickname;//用户昵称，基于微信
    private String OpenId;//用户唯一识别码，基于微信
    private String Code;//用户临时获取的登录凭证，基于微信
    private String Province;//用户的省份，基于微信
    private String City;//用户的城市，基于微信
    private String Session;//用户的登录session保存，基于微信
    private int Authority;//用户的权限，其中1为普通用户
    private String School;//用户所在的学校，基于用户本身
    private String Name;//用户的个人姓名，基于用户本身
    private String Gender;//用户的性别，基于微信
    private String Schoolid;//用户学号，基于用户本身，未来可能废弃
    private String Image;//用户头像存放
    private String JWTToken;
    private int peoplenumber;
    @Column(columnDefinition = "DATETIME NOT NULL " + "DEFAULT CURRENT_TIMESTAMP ON UPDATE " + "CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;//资料最后更新时间（上次登录时间）
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次登录时间

    @OneToMany(mappedBy = "WechatUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserRecord> UserRecord;
    @OneToMany(mappedBy = "WechatUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Integral> Integral;

    public WechatUser(int id) {
        this.id = id;
    }

}
