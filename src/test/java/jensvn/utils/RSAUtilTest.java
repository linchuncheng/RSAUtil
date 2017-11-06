package jensvn.utils;

import org.junit.jupiter.api.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import utils.RSAUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

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
    public void testGenKeyPair() throws Exception {
        RSAUtil.genKeyPair("test");
    }

    /**
     * 测试加解密
     */
    @Test
    public void testEncryption() throws Exception {
        String data = "..123ABC加解密测试";
        // 加载私钥、公钥
        PrivateKey privateKey = RSAUtil.loadPrivateKey("test.prikey");
        PublicKey publicKey = RSAUtil.loadPublicKey("test.pubkey");
        // 数据加密
        byte[] encryptData = RSAUtil.encrypt(privateKey, data.getBytes());
        // 数据传输使用Base64编码传输字符串
        String encryptDataString = new BASE64Encoder().encode(encryptData);
        // 数据解密
        byte[] decryptData = RSAUtil.decrypt(publicKey, new BASE64Decoder().decodeBuffer(encryptDataString));
        System.out.println("加密前:" + data);
        System.out.println("加密后:" + encryptDataString);
        System.out.println("解密后:" + new String(decryptData));
    }

}
