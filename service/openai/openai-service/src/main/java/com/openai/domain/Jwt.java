package com.openai.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XuJ
 */
@Data
public class Jwt {
    /**
     * 加密时候用  是对称的秘钥盐
     */
    public static final String SECRET = "OpenAiqwertyuiopasdfghjklzxcvbnm";
    /**
     * 加密算法 HS256
     * 用来说明这个JWT签发的时候所使用的签名和摘要算法
     */
    public static final String ALG = "HS256";
    /**
     * 用来标识整个token字符串是一个JWT字符串
     */
    public static final String TYP = "JWT";
    public static final Map<String, Object> HEADER = new HashMap<>();
    private String userName = "root";
    private String passWord = "root";
    private String apiKey;

    public Jwt(String userName, String passWord, String apiKey) {
        this.userName = userName;
        this.passWord = passWord;
        this.apiKey = apiKey;
    }

    public Jwt(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, Object> getJwtHeader() {
        if (HEADER.isEmpty()) {
            HEADER.put("alg", ALG);
            HEADER.put("typ", TYP);
        }
        return HEADER;
    }
    public Map<String, Object> getJwtClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", this.userName);
        claims.put("passWord", this.passWord);
        claims.put("apiKey", this.apiKey);
        //标准中注册的声明
        claims.put("iss", "lee");
        return claims;
    }
}
