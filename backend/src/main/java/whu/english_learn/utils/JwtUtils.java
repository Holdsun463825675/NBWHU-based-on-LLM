package whu.english_learn.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static String signKey = "nbwhu";
    private static Long expire = 43200000L;
    /**
     *生成JWT令牌
     * @Param claims JWT第二部分负载payload中存储的内容
     */
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder().
                addClaims(claims).
                signWith(SignatureAlgorithm.HS256, signKey).
                setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }
    /**
     *解析JWT令牌
     * @Param jwt jwt令牌
     */
    public static Claims parseJwt(String jwt) {
       Claims claims = Jwts.parser()
               .setSigningKey(signKey)
               .parseClaimsJws(jwt)
               .getBody();
       return claims;
    }
}
