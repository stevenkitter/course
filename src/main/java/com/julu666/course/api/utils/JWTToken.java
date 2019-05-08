package com.julu666.course.api.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTToken {

    public static Key key = new SecretKeySpec("desk23123dc&213*211wall22desk23123dc&213*211wall22desk23123dc&213*211wall22".getBytes(),
            SignatureAlgorithm.HS256.getJcaName());

    public static String generateToken(String userId) {
        Date exp = DateOperation.getDateAfter(new Date(), 2);

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);

        JwtBuilder jwt = Jwts.builder()
                .setExpiration(exp)
                .setIssuedAt(new Date())
                .signWith(key)
                .setClaims(claims);
        return jwt.compact();
    }

    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        return claims;
    }

    public static String userId(String authorization) {
        String[] tokens = authorization.split(" ");
        if (tokens.length != 2) {
            return "";
        }
        String token = tokens[1];
        Claims claims = JWTToken.parseJWT(token);
        if (claims.get("userId").toString() == "") {
            return "";
        }
        return claims.get("userId").toString();
    }
}
