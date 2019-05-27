package com.ultronxr.accountencryption.utils.db.bean;

public class Record {

    private int id;                   //ID
    private String create_time;       //创建时间
    private String last_modify_time;  //最后修改时间
    private String category;          //分类
    private String account_name;      //账户名称
    private String account_num;       //账号
    private String account_pwd;       //密码
    private String nick;              //昵称
    private String email;             //邮箱
    private String phone;             //手机号
    private String url;               //网址
    private String security_problem;  //密保问题
    private String security_answer;   //密保问题密码
    private String note;              //其他


    public Record() {
    }

    public Record(String last_modify_time, String category, String account_name, String account_num, String account_pwd, String nick, String email, String phone, String url, String security_problem, String security_answer, String note) {
        this.last_modify_time = last_modify_time;
        this.category = category;
        this.account_name = account_name;
        this.account_num = account_num;
        this.account_pwd = account_pwd;
        this.nick = nick;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.security_problem = security_problem;
        this.security_answer = security_answer;
        this.note = note;
    }

    public Record(int id, String create_time, String last_modify_time, String category, String account_name, String account_num, String account_pwd, String nick, String email, String phone, String url, String security_problem, String security_answer, String note) {
        this.id = id;
        this.create_time = create_time;
        this.last_modify_time = last_modify_time;
        this.category = category;
        this.account_name = account_name;
        this.account_num = account_num;
        this.account_pwd = account_pwd;
        this.nick = nick;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.security_problem = security_problem;
        this.security_answer = security_answer;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLast_modify_time() {
        return last_modify_time;
    }

    public void setLast_modify_time(String last_modify_time) {
        this.last_modify_time = last_modify_time;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = account_num;
    }

    public String getAccount_pwd() {
        return account_pwd;
    }

    public void setAccount_pwd(String account_pwd) {
        this.account_pwd = account_pwd;
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

    public String getSecurity_problem() {
        return security_problem;
    }

    public void setSecurity_problem(String security_problem) {
        this.security_problem = security_problem;
    }

    public String getSecurity_answer() {
        return security_answer;
    }

    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
