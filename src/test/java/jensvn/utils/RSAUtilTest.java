package jensvn.utils;

import org.junit.jupiter.api.Test;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA加解密工具类
 *
 * @author jensvn@qq.com on 2017/11/3.
 * @version 1.0
 * @date 2017/11/3.
 */
public class RSAUtilTest {

    /**
     * 测试生成RSA密钥对
     */
    @Test
    public void testGenKeyPair() {
        try {
            RSAUtil.genKeyPair("/test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试加解密
     */
    @Test
    public void testEncryption() {
        byte[] data = "..123ABC加解密测试".getBytes();
        System.out.println("解密前:" + new String(data));
        try {
            // 加载私钥、公钥
            RSAPrivateKey privateKey = RSAUtil.loadPrivateKey(new FileInputStream("/test.prikey"));
            RSAPublicKey publicKey = RSAUtil.loadPublicKey(new FileInputStream("/test.pubkey"));
            // 加密
            byte[] encryptData = RSAUtil.encrypt(data, privateKey);
            // 使用Base64编码打印，数据传输使用Base64编码传输字符串
            System.out.println("加密后:" + new BASE64Encoder().encode(encryptData));
            // 解密
            byte[] decryptData = RSAUtil.decrypt(encryptData, publicKey);
            System.out.println("解密后:" + new String(decryptData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
