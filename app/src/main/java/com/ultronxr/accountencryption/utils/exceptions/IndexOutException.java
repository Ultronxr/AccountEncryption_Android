package com.ultronxr.accountencryption.utils.exceptions;

/**
 * 数组下标越界异常
 * 下标小于零，或者大于最大下标
 */
public class IndexOutException extends Exception{

    private int value;

    public IndexOutException(){
        super();
    }

    public IndexOutException(String msg, int value){
        super(msg);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
