package utils;

import org.junit.jupiter.api.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA加解密工具测试类
 *
 * @author jensvn@qq.com on 2017/11/3.
 * @version 1.0
 * @date 2017/11/3.
 */
public class RSAUtil2Test {

    /**
     * 测试加解密
     */
    @Test
    public void testEncryption() throws Exception {
        String data = "RSA加密数据有长度限制，使用AES加密数据得到加密Data和KEY，使用RSA加密KEY得到加密Key，数据传输使用加密Key和加密Data，解密Data使用KEY";
        System.out.println("data.getBytes().length:" + data.getBytes().length);
        // 密钥库
        KeyStore keyStore = RSAUtil2.loadKeyStore("test.keystore", "123456");
        // 私钥
        PrivateKey privateKey = RSAUtil2.getPrivateKey(keyStore, "test", "123456");
        // 公钥：服务端的公钥是通过KeyStore获得
        PublicKey publicKey_server = keyStore.getCertificate("test").getPublicKey();
        // 公钥：客户端的公钥是通过证书获得
        PublicKey publicKey_client = RSAUtil2.loadCertificate("test.crt").getPublicKey();
        // 加密
        byte[] encryptData = RSAUtil2.encrypt(privateKey, data.getBytes());
        // Base64编码后传输
        String encryptDataString = new BASE64Encoder().encode(encryptData);
        // 解密
        byte[] decryptData = RSAUtil2.decrypt(publicKey_client, new BASE64Decoder().decodeBuffer(encryptDataString));

        System.out.println("加密前:" + data);
        System.out.println("加密后:" + encryptDataString);
        System.out.println("解密后:" + new String(decryptData));
    }

}
