package com.clj.reptilehouse.common.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class PubDecode{
	private static final String KEY_ALGORITHM = "RSA";
	private static final String split = " ";//分隔符
	private static final int max = 117;//加密分段长度//不可超过117
	//公钥，不可更改
	private static final String pubKey ="30819F300D06092A864886F70D010101050003818D003081890281810088D39CD8B61008544724C4EB620C326890F3C8A6B261A936EC4B77201B23ECF2D4DE3ABDED89A8C528171CCBCB6109CBE329EDE36A51CD284F35AB8640ECCAAF6450BB5D19D6B959E45E0FDFD1F0FBB15D06817809E9CB6979210AD74148D1785F40B07FC054A8395A3DA8749175ABD330C03A7A2EE9261AE291799C31B0E1FF0203010001";
	 /**解密-公钥*/
    public static String decodeByPublicKey(String res){
        byte[] keyBytes = parseHexStr2Byte(pubKey);
        //先分段
        String[] rs = res.split("\\"+split);
        //分段解密
        if(rs!=null){
            int len = 0;
            //组合byte[]
            byte[] result = new byte[rs.length*max];
            for (int i = 0; i < rs.length; i++) {
                byte[] bs = decodePub(parseHexStr2Byte(rs[i]), keyBytes);
                if(bs==null) return null;
                System.arraycopy(bs, 0, result, i*max, bs.length);
                len+=bs.length;
            }
            byte[] newResult = new byte[len];
            System.arraycopy(result, 0, newResult, 0, len);
            //还原字符串
            return new String(newResult);
        }
        return null;
    }
    
    /**将16进制转换为二进制*/
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
            result[i] = (byte) (high * 16 + low);  
        }
        return result;  
    }
    
    /**解密-公钥-无分段*/
    private static byte[] decodePub(byte[] res,byte[] keyBytes){
        X509EncodedKeySpec x5 = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
            Key pubKey = kf.generatePublic(x5);
            Cipher cp = Cipher.getInstance(kf.getAlgorithm());
            cp.init(Cipher.DECRYPT_MODE, pubKey);
            return cp.doFinal(res);
        } catch (Exception e) {
            System.out.println("公钥解密失败");
            //e.printStackTrace();
        }
        return null;
    }
}
