package com.everwing.utils;/**
 * Created by wust on 2018/2/2.
 */

import java.io.*;

/**
 *
 * Function:
 * Reason:
 * Date:2018/2/2
 * @author wusongti@lii.com.cn
 */
public class SerializeUtil {
    private SerializeUtil(){}

    //序列化方法
    public static byte[] object2Bytes(Object value) throws IOException {
        if (value == null)
            return null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(value);
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayOutputStream.toByteArray();
    }

    //反序列化方法
    public static Object byte2Object(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0)
            return null;
        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inputStream.readObject();
            return obj;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
