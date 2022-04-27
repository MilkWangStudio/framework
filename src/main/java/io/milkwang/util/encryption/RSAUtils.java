package io.milkwang.util.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类，字符串转换使用到了Base64Utils。
 * RSA属于对称加密算法，可以进行解密
 */
public class RSAUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);
    public static final String RSA_ALGORITHM = "RSA";
    public static final Charset UTF8 = StandardCharsets.UTF_8;

    public static void main(String[] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // encrypt the message
        String encrypted = encryptToString(publicKey, "This is a secret message");
        System.out.println(encrypted);  // <<encrypted message>>

        // decrypt the message
        String secret = decryptFromStringToString(privateKey, encrypted);
        System.out.println(secret);     // This is a secret message
    }

    /**
     * 构建密钥对, 默认keySize=2048, 支持2048/8-11=245个字符，一个汉字算两个字符
     *
     * @return 密钥对
     * @throws NoSuchAlgorithmException 未找到算法
     */
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        return buildKeyPair(keySize);
    }

    /**
     * 构建密钥对, 最多支持x个字符进行加密，x=keySize/8-11，一个汉字算两个字符
     *
     * @return 密钥对
     * @throws NoSuchAlgorithmException 未找到算法
     */
    public static KeyPair buildKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * 使用公钥对字符串加密，返回经过base64编码后的字符串
     *
     * @param publicKey 公钥
     * @param message   待加密字符串
     * @return 编码后的加密字符串
     * @throws GeneralSecurityException 通用Security异常
     */
    public static String encryptToString(PublicKey publicKey, String message) throws GeneralSecurityException {
        return Base64Utils.encodeToString(encrypt(publicKey, message));
    }


    /**
     * 使用公钥对字符串加密
     *
     * @param publicKey 公钥
     * @param message   待加密字符串
     * @return 加密后的byte数组
     * @throws GeneralSecurityException 通用Security异常
     */
    public static byte[] encrypt(PublicKey publicKey, String message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes(UTF8));
    }

    /**
     * 使用base64解码加密后字符串，得到byte数组后，再使用私钥进行解码，最后使用UTF-8将byte数组转换为字符串
     *
     * @param privateKey 私钥
     * @param encrypted  加密后字符串
     * @return 解密后字符串
     * @throws GeneralSecurityException 通用Security异常
     */
    public static String decryptFromStringToString(PrivateKey privateKey, String encrypted) throws GeneralSecurityException {
        byte[] bytes = decryptFromString(privateKey, encrypted);
        return new String(bytes, UTF8);
    }

    /**
     * 对加密后的字符串，先用base64解码，然后再使用私钥进行rsa解码
     *
     * @param privateKey 私钥
     * @param encrypted  加密后经过base64编码的byte数组字符串
     * @return 解码后的byte数组
     * @throws GeneralSecurityException 通用Security异常
     */
    public static byte[] decryptFromString(PrivateKey privateKey, String encrypted) throws GeneralSecurityException {
        byte[] decode = Base64Utils.decode(encrypted);
        return decrypt(privateKey, decode);
    }

    /**
     * 用私钥对加密byte数组解密
     *
     * @param privateKey 私钥
     * @param encrypted  加密后的byte数组
     * @return 解密后的byte数组
     * @throws GeneralSecurityException 通用Security异常
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 加密后的公钥字符串
     * @return 公钥
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr 加密后的私钥字符串
     * @return 私钥
     */
    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) {
        try {
            byte[] buffer = Base64Utils.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = null;
            keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 得到公钥字符串，将byte[]编码后处理
     *
     * @param publicKey 公钥
     * @return 字符串，可以存储到配置中心
     */
    public String getPublicKeyString(PublicKey publicKey) {
        return Base64Utils.encodeToString(publicKey.getEncoded());
    }

    /**
     * 得到私钥字符串，将byte[]编码后处理
     *
     * @param privateKey 私钥
     * @return 字符串，可以存储到配置中心
     */
    public String getPrivateKeyString(PrivateKey privateKey) {
        return Base64Utils.encodeToString(privateKey.getEncoded());
    }

}
