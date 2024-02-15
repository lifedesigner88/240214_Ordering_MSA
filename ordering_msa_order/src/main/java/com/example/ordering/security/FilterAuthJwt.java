package com.example.ordering.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ordering.common.ErrResponseMessage.makeMessage;

@Component
public class FilterAuthJwt extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filter)
            throws IOException, ServletException {

        try {
            String token = ((HttpServletRequest) req).getHeader("Authorization");
            // check if the header is null or doesn't start with "Bearer "

            if(token != null) {
                if (!token.startsWith("Bearer "))
                    throw new AuthenticationServiceException("The token is invalid or not present.");
                token = token.substring(7);

//                토큰 검증 및 claims 추출
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

//                Authentication 객체를 생성하기 위한 UserDetails 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                "",
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filter.doFilter(req, res);

        } catch (Exception e) {
            HttpServletResponse httpRes = (HttpServletResponse) res;
            httpRes.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpRes.setContentType("application/json");
            httpRes.getWriter()
                    .write(
                            makeMessage(
                                    HttpStatus.UNAUTHORIZED,
                                    e.getMessage()
                            ).toString()
                    );
        }
    }
}