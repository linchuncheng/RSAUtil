package jensvn.utils;

import java.io.*;

/**
 * 字符数组工具类
 *
 * @author jensvn@qq.com on 2017/11/3.
 * @version 1.0
 * @date 2017/11/3.
 */
public class BytesUtils {
    /**
     * 对象转byte数组
     *
     * @param obj
     * @return
     */
    public static byte[] toBytes(Object obj) {
        byte[] bytes = null;
        try {
            // object to byte array
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte数组转对象
     *
     * @param bytes
     * @return
     */
    public static <T> T toObject(byte[] bytes) {
        T t = null;
        try {
            // byte array to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            t = (T) oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    public static byte[] toBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
