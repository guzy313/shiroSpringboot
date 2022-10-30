package com.my.shirospringboot.pojo;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author guzy
 * @version 1.0
 * @description shiroUsers 用户表
 */
@TableName("sh_users")
public class ShUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键
    private String id;
    //登录名
    private String loginName;
    //真实姓名
    private String realName;
    //昵称
    private String nickName;
    //密码
    private String password;
    //加密因子
    private String salt;
    //性别
    private String sex;
    //邮政编码
    private String zipCode;
    //地址
    private String address;
    //固定电话
    private String tel;
    //手机号码
    private String phone;
    //电子邮箱
    private String email;
    //职务
    private String job;
    //排序
    private Integer sort;
    //是否有效
    private String enableFlag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }



    @Override
    public String toString() {
        return "ShUsers{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", sex=" + sex +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", job='" + job + '\'' +
                ", sort=" + sort +
                ", enableFlag='" + enableFlag + '\'' +
                '}';
    }
}
