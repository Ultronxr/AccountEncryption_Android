package com.ultronxr.accountencryption.utils.db.bean;

public class Encryptor {

    private int id;
    private String pwd;
    private String createTime;
    private String lastModifyTime;

    public Encryptor() {
    }

    public Encryptor(String pwd) {
        this.pwd = pwd;
    }

    public Encryptor(int id, String pwd, String createTime, String lastModifyTime) {
        this.id = id;
        this.pwd = pwd;
        this.createTime = createTime;
        this.lastModifyTime = lastModifyTime;
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
}
