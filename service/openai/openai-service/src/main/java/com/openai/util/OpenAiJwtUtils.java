package com.openai.util;

import com.openai.domain.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class OpenAiJwtUtils {

    /**
     * 创建jwt
     */
    public static String createJwtToken(Jwt jwt) {
        //生成jwt
        return Jwts.builder()
                // 添加Header信息
                .setHeader(jwt.getJwtHeader())
                // 添加Payload信息
                .setClaims(jwt.getJwtClaims())
                // 设置jti：是JWT的唯一标识
                .setId(UUID.randomUUID().toString())
                // 设置iat: jwt的签发时间
                .setIssuedAt(new Date())
                // 设置exp：jwt过期时间，3600秒=1小时
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 24))
                //设置sub：代表这个jwt所面向的用户
//                .setSubject("Jack")
                //设置签名：通过签名算法和秘钥生成签名
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET).compact();
    }

    /**
     * 从jwt中获取 Payload 信息
     */
    private static Claims getClaimsFromJwt(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(Jwt.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 从jwt中获取ApiKey
     */
    private static String getApiKeyFromJwtClaims(String jwt) {
        return getClaimsFromJwt(jwt).get("apiKey",String.class);
    }

    public static void main(String[] args) {
        String jwtToken = createJwtToken(new Jwt("1111111"));
        System.out.println("JWT Token: " + jwtToken);
        System.out.println("=======================================================");

        Claims claims = getClaimsFromJwt(jwtToken);
        System.out.println("claims: " + claims);
    }
}