package com.ultronxr.accountencryption.utils.db.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {

    private int id;                   //ID
    private String createTime;        //创建时间
    private String lastModifyTime;    //最后修改时间
    private String category;          //分类
    private String accountName;       //账户名称
    private String accountNum;        //账号，账号和账号下面的数据存放时都需要加密，读取时都需要解密
    private String accountPwd;        //密码
    private String nick;              //昵称
    private String email;             //邮箱
    private String phone;             //手机号
    private String url;               //网址
    private String securityProblem;   //密保问题
    private String securityAnswer;    //密保问题密码
    private String note;              //其他


    public Record() {
    }


    public Record(String lastModifyTime, String category, String accountName, String accountNum, String accountPwd, String nick, String email, String phone, String url, String securityProblem, String securityAnswer, String note) {
        this.lastModifyTime = (lastModifyTime==null) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : lastModifyTime;
        this.category = (category==null) ? "" : category;
        this.accountName = (accountName==null) ? "" : accountName;
        this.accountNum = (accountNum==null) ? "" : accountNum;
        this.accountPwd = (accountPwd==null) ? "" : accountPwd;
        this.nick = (nick==null) ? "" : nick;
        this.email = (email==null) ? "" : email;
        this.phone = (phone==null) ? "" : phone;
        this.url = (url==null) ? "" : url;
        this.securityProblem = (securityProblem==null) ? "" : securityProblem;
        this.securityAnswer = (securityAnswer==null) ? "" : securityAnswer;
        this.note = (note==null) ? "" : note;
    }

    public Record(int id, String createTime, String lastModifyTime, String category, String accountName, String accountNum, String accountPwd, String nick, String email, String phone, String url, String securityProblem, String securityAnswer, String note) {
        this.id = id;
        this.createTime = createTime;
        this.lastModifyTime = (lastModifyTime==null) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : lastModifyTime;
        this.category = (category==null) ? "" : category;
        this.accountName = (accountName==null) ? "" : accountName;
        this.accountNum = (accountNum==null) ? "" : accountNum;
        this.accountPwd = (accountPwd==null) ? "" : accountPwd;
        this.nick = (nick==null) ? "" : nick;
        this.email = (email==null) ? "" : email;
        this.phone = (phone==null) ? "" : phone;
        this.url = (url==null) ? "" : url;
        this.securityProblem = (securityProblem==null) ? "" : securityProblem;
        this.securityAnswer = (securityAnswer==null) ? "" : securityAnswer;
        this.note = (note==null) ? "" : note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecurityProblem() {
        return securityProblem;
    }

    public void setSecurityProblem(String securityProblem) {
        this.securityProblem = securityProblem;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", lastModifyTime='" + lastModifyTime + '\'' +
                ", category='" + category + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountNum='" + accountNum + '\'' +
                ", accountPwd='" + accountPwd + '\'' +
                ", nick='" + nick + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", securityProblem='" + securityProblem + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
