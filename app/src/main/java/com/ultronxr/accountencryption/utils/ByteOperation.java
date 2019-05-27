package com.ultronxr.accountencryption.utils;


import com.ultronxr.accountencryption.utils.exceptions.IndexOutException;
import com.ultronxr.accountencryption.utils.exceptions.TargetLengthNegativeException;

public class ByteOperation {

    /**
     * 获取一个byte数组的子数组（截取一部分）
     * @param srcBytesArray 原byte数组
     * @param indexStart    起始下标，从0开始
     * @param length        截取的长度
     * @exception IndexOutException 截取部分的下标超出了原数组的范围
     * @exception TargetLengthNegativeException 待截取的目标长度为负
     * @return 截取后的子byte数组
     */
    public static byte[] getSubBytesArray(byte[] srcBytesArray, int indexStart, int length)
            throws TargetLengthNegativeException, IndexOutException {

        if(length < 0)
            throw new TargetLengthNegativeException("目标长度不得小于零！", -1);
        if(indexStart > srcBytesArray.length-1 || indexStart+length > srcBytesArray.length || indexStart < 0)
            throw new IndexOutException("数组下标越界！", -1);

        byte[] subBytesArray = new byte[length];
        System.arraycopy(srcBytesArray, indexStart, subBytesArray, 0, length);
        return subBytesArray;

    }

//    public static void main(String[] args){
//        byte[] src = {1,2,3,4,5,6,7,8};
//        try{
//            byte[] target = getSubBytesArray(src, 8, 0);
//            for(byte i : target)
//                System.out.println(i);
//        }catch (TargetLengthNegativeException | IndexOutException e) { e.printStackTrace(); }
//
//    }

}
