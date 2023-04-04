package com.star.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XuJ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jwt {

    private String userName = "root";
    private String passWord = "root";
    private String apiKey;

    public Map<String, Object> getJwtClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", this.userName);
        claims.put("passWord", this.passWord);
        claims.put("apiKey", this.apiKey);
        //标准中注册的声明
        claims.put("iss", "lee");
        return claims;
    }

    public Jwt(String apiKey) {
        this.apiKey = apiKey;
    }
}
