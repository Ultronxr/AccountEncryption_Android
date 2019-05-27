package com.ultronxr.accountencryption.utils.exceptions;

/**
 * 目标长度为负异常
 * 例如截取数组时长度指定为-1
 */
public class TargetLengthNegativeException extends Exception{

    private int value;

    public TargetLengthNegativeException(){
        super();
    }

    public TargetLengthNegativeException(String msg, int value){
        super(msg);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
