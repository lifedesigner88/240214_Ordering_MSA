package com.example.ordering.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Slf4j
public class TokenProviderJwt {


    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;


    public String createdToken(String email, String role){
//        claims: 클레임은 토큰 사용자에 대한 속성이나 데이터 포함, 주로 페이로드를 의미.

         log.info(secretKey);
         log.info("만료기간(분)" + expiration);

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L ) )
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
