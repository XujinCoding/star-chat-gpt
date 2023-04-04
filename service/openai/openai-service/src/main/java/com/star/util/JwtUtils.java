package com.star.util;

import com.star.domain.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * @author XuJ
 */
@Slf4j
public class JwtUtils {
    private static final long EXPIPE_TIME=3600 * 1000 * 24;
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
    /**
     * 创建jwt
     */
    public static String createJwtToken(Jwt jwt) {
        //生成jwt
        return Jwts.builder()
                // 添加Header信息
                .setHeader(getJwtHeader())
                // 添加Payload信息
                .setClaims(jwt.getJwtClaims())
                // 设置jti：是JWT的唯一标识
                .setId(UUID.randomUUID().toString())
                // 设置iat: jwt的签发时间
                .setIssuedAt(new Date())
                // 设置exp：jwt过期时间，3600秒=1小时
                .setExpiration(new Date(System.currentTimeMillis() + EXPIPE_TIME))
                //设置sub：代表这个jwt所面向的用户
//                .setSubject("Jack")
                //设置签名：通过签名算法和秘钥生成签名
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    /**
     * 从jwt中获取 Payload 信息
     */
    private static Claims getClaimsFromJwt(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("------------解析Jwt令牌出错");
            e.printStackTrace();
        }
        return claims;
    }
    public static Map<String, Object> getJwtHeader() {
        if (HEADER.isEmpty()) {
            HEADER.put("alg", ALG);
            HEADER.put("typ", TYP);
        }
        return HEADER;
    }

    /**
     * 从jwt中获取ApiKey
     * 检查包括:
     *  1. token是否为null;
     *  2. token是否过期了;
     */
    public static boolean validateToken(String token ) {
        return !StringUtils.isEmpty(token) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private static  boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private static Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromJwt(token);
        return claims.getExpiration();
    }

    public static String getApiKeyFromJwtClaims(String token) {
        return getClaimsFromJwt(token).get("apiKey",String.class);
    }
    public static String getUserNameFromJwtClaims(String token) {
        return getClaimsFromJwt(token).get("userName",String.class);
    }
}