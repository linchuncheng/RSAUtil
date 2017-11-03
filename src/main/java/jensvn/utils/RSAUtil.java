package jensvn.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA加解密工具类
 *
 * @author jensvn@qq.com on 2017/11/3.
 * @version 1.0
 * @date 2017/11/3.
 */
public class RSAUtil {
    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static void genKeyPair(String path) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        FileOutputStream fos1 = new FileOutputStream(path + ".pubkey");
        FileOutputStream fos2 = new FileOutputStream(path + ".prikey");
        fos1.write(BytesUtils.toBytes(publicKey));
        fos2.write(BytesUtils.toBytes(privateKey));
        fos1.close();
        fos2.close();
        System.out.println("公钥生成成功!公钥文件为：" + path + ".pubkey");
        System.out.println("私钥生成成功!私钥文件为：" + path + ".prikey");
    }

    public static RSAPublicKey loadPublicKey(InputStream is) throws Exception {
        return BytesUtils.toObject(BytesUtils.toBytes(is));
    }

    public static RSAPrivateKey loadPrivateKey(InputStream is) throws Exception {
        return BytesUtils.toObject(BytesUtils.toBytes(is));
    }

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        out.close();
        return out.toByteArray();
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = data.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        out.close();
        return out.toByteArray();
    }
}