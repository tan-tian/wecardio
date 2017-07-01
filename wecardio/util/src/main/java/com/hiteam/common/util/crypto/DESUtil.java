package com.hiteam.common.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * DES加密工具
 * 此文件里要求密钥长度必须>=8位，并非此程序内对密钥进行了8位长度的截取，而是jdk DES加密算法内部默认只取8位
 * @version Jul 9, 2013
 */
public class DESUtil {

    /**
     * 定义 加密算法,可用 DES,DESede(三重DES加密),Blowfish
     */
    private static final String ALGORITHM = "DES";

    /**
     * 乱序MAP
     */
    private static Map<String, String> map = new HashMap<String, String>();

    /**
     * 统一编码
     */
    private static final String CHARCODE = "UTF-8";

    static {
        map.put("0", "E");
        map.put("1", "6");
        map.put("2", "C");
        map.put("3", "9");
        map.put("4", "5");
        map.put("5", "O");
        map.put("6", "Y");
        map.put("7", "M");
        map.put("8", "G");
        map.put("9", "S");
        map.put("A", "A");
        map.put("B", "T");
        map.put("C", "3");
        map.put("D", "S");
        map.put("E", "V");
        map.put("F", "Q");
    }

    /**
     * 根据密钥解密密文
     *
     * @param keybyte 字节数组密钥,要求UTF-8编码格式
     * @param src     密文
     * @return 解密后的明文, 解密失败返回null
     */
    public static String decrypt(byte[] keybyte, String src) {
        String de = decryStrMode(keybyte, src);
        return de;
    }

    /**
     * 根据配置文件中配置的密钥接口类，拿到密钥进行解密
     *
     * @param key 密钥
     * @param src 密文
     * @return 返回解密后的明文, 如果解密失败返回null
     */
    public static String decrypt(String key, String src) {
        return decrypt(getBytes(key), src);
    }

    /**
     * 根据密钥加密获取密文
     *
     * @param key 密钥
     * @param src 加密明文
     * @return 密文
     */
    public static String encrypt(String key, String src) {
        return encryptStrMode(getBytes(key), getBytes(src));
    }

    /**
     * 加密方法
     *
     * @param keybyte 为加密密钥
     * @param src     为被加密的数据缓冲区
     * @return 加密后的密文
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {

        try {
            //DES算法要求有一个可信任的随机数源
            SecureRandom sr2 = new SecureRandom();
            //从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(keybyte);
            //创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr2);
            //正式执行加密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密方法（加密源字符串需要为8的倍数，此方法用于自动补全字符串长度）
     *
     * @param keybyte 为加密密钥
     * @param src     为被加密的数据缓冲区
     * @return 加密后的密文
     */
    private static String encryptStrMode(byte[] keybyte, byte[] src) {
        String s = "";
        int m = src.length / 8;
        int n = src.length % 8;
        if (m > 0) {
            //以下3句不可用 new String(src,0,m*8,"utf-8").getBytes()代替，因为得到的byte[]长度不一定是8的倍数，不是8的倍数，将报异常
            int cnt = m * 8;
            byte[] bys = new byte[cnt]; //必须是8的倍数
            for (int i = 0; i < cnt; i++) {
                bys[i] = src[i];
            }
            s = byte2hex(encryptMode(keybyte, bys));
        }
        if (n != 0) { //如果不满足8的倍数，则补0
            byte[] bys = new byte[8];
            int i = 0;
            for (; i < n; i++) {
                bys[i] = src[m * 8 + i];
            }
            while (i < 8) {
                bys[i++] = 0;
            }
            s = s + byte2hex(encryptMode(keybyte, bys));
        }
        return s;
    }


    /**
     * 解密方法
     *
     * @param keybyte 为加密密钥
     * @param src     为加密后的缓冲区
     * @return 解密后的明文, 解密失败返回null
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {

        try {
            //DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            //从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(keybyte);
            //创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);
            //Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            //正式执行解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密方法（去掉加密时为补全长度为8的倍数而加的0）
     *
     * @param keybyte 为加密密钥
     * @param src     为加密后的缓冲区
     * @return 解密后的明文, 解密失败返回null
     */
    private static String decryStrMode(byte[] keybyte, String src) {
        try {
            byte[] b = decryptMode(keybyte, hex2byte(src));
            int i = 0;
            //去掉后面的 byte[0]
            while (i < b.length && b[b.length - 1 - i] == 0) {
                i++;
            }
            return new String(b, 0, b.length - i, CHARCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 2进制数组转换成16进制字符串
     *
     * @param b 2进制数组
     * @return 16进制字符串
     */
    public static String byte2hex(byte[] b) {
        // 转成16进制字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase(); // 转成大写
    }

    /**
     * 16进制字符串转换成2进制数组
     *
     * @param hexStr 16进制字符串
     * @return 2进制数组
     */
    public static byte[] hex2byte(String hexStr) throws Exception {
        byte[] b = getBytes(hexStr);
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2, CHARCODE);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 字符串转byte[]
     *
     * @param str 被转的字符串
     */
    public static byte[] getBytes(String str) {
        try {
            byte[] keybyte = str.getBytes(CHARCODE);
            return keybyte;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
