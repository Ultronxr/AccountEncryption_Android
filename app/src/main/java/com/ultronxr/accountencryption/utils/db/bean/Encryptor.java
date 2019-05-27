package com.ultronxr.accountencryption.utils.db.bean;

public class Encryptor {

    private int id;
    private String pwd;
    private String create_time;
    private String last_modify_time;

    public Encryptor() {
    }

    public Encryptor(String pwd, String last_modify_time) {
        this.pwd = pwd;
        this.last_modify_time = last_modify_time;
    }

    public Encryptor(int id, String pwd, String create_time, String last_modify_time) {
        this.id = id;
        this.pwd = pwd;
        this.create_time = create_time;
        this.last_modify_time = last_modify_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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
}
