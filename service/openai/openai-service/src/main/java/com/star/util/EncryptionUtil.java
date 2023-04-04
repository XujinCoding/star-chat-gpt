package com.star.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
@Component
public class EncryptionUtil {
    private static String strDefaultKey = "UvVi-s`75X-d9fO3";

    /** 加密工具 */
    private ThreadLocal<Cipher> encryptCipher = new ThreadLocal<Cipher>() {
        @Override
        protected Cipher initialValue() {
            try {
                Cipher encryptCipher = Cipher.getInstance(algorithmName);
                encryptCipher.init(Cipher.ENCRYPT_MODE, key);
                return encryptCipher;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    /** 解密工具 */
    private ThreadLocal<Cipher> decryptCipher = new ThreadLocal<Cipher>() {
        @Override
        protected Cipher initialValue() {
            try {
                Cipher decryptCipher = Cipher.getInstance(algorithmName);
                decryptCipher.init(Cipher.DECRYPT_MODE, key);
                return decryptCipher;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    private String algorithmName;
    private Key key;

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuilder sb = new StringBuilder(arrB.length * 2);
        for (int b : arrB) {
            int intTmp = b;
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     *
     */
    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    public EncryptionUtil() throws Exception {
        this("AES/ECB/PKCS5Padding", strDefaultKey, 128);
    }

    /**
     * 指定密钥构造方法
     *
     * @param strKey  指定的密钥
     * @throws Exception
     */
    public EncryptionUtil(String algorithmName, String strKey, int keySizeInBits) throws Exception {
        this.algorithmName = algorithmName;
        this.key = getKey(strKey.getBytes(StandardCharsets.UTF_8), keySizeInBits);
    }

    /**
     * 加密字节数组
     *
     * @param arrB
     *            需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.get().doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn
     *            需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     *
     * @param arrB
     *            需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.get().doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn
     *            需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @param keySizeInBits
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp, int keySizeInBits) throws Exception {
        // 创建一个空的字节数组（默认值为0）
        byte[] arrB = new byte[keySizeInBits / 8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        String keyAlg = algorithmName;
        int t = algorithmName.indexOf('/');
        if (t != -1) {
            keyAlg = algorithmName.substring(0, t);
        }

        return new SecretKeySpec(arrB, keyAlg);
    }
}
