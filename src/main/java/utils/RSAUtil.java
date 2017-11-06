package utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类
 * 私钥加密公钥解密，或公钥加密私钥解密
 *
 * @author jensvn@qq.com on 2017/11/3.
 * @version 1.0
 * @date 2017/11/3.
 */
public class RSAUtil {
    public static final BASE64Encoder ENCODER = new BASE64Encoder();
    public static final BASE64Decoder DECODER = new BASE64Decoder();
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
    public static void genKeyPair(String dir) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 密钥位数
        keyPairGen.initialize(1024);
        // 密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        //得到公钥字符串
        String publicKeyString = ENCODER.encode(publicKey.getEncoded());
        //得到私钥字符串
        String privateKeyString = ENCODER.encode(privateKey.getEncoded());
        //将密钥对写入到文件
        FileWriter pubfw = new FileWriter(dir + ".pubkey");
        FileWriter prifw = new FileWriter(dir + ".prikey");
        BufferedWriter pubbw = new BufferedWriter(pubfw);
        BufferedWriter pribw = new BufferedWriter(prifw);
        pubbw.write(publicKeyString);
        pribw.write(privateKeyString);
        pubbw.flush();
        pubbw.close();
        pubfw.close();
        pribw.flush();
        pribw.close();
        prifw.close();
        System.out.println("公钥生成成功!公钥文件为：" + dir + ".pubkey");
        System.out.println("私钥生成成功!私钥文件为：" + dir + ".prikey");
    }

    /**
     * 加载公钥
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(String filePath) throws Exception {
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder keyString = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            keyString.append(str);
        }
        br.close();
        fr.close();
        return getPublicKey(keyString.toString());
    }

    /**
     * 加载私钥
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String filePath) throws Exception {
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder keyString = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            keyString.append(str);
        }
        br.close();
        fr.close();
        return getPrivateKey(keyString.toString());
    }

    /**
     * 获取公钥
     *
     * @param publicKeyString
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = DECODER.decodeBuffer(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKeyString
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = DECODER.decodeBuffer(privateKeyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 加密
     */
    public static byte[] encrypt(Key key, byte[] data) throws Exception {
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
    public static byte[] decrypt(Key key, byte[] data) throws Exception {
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