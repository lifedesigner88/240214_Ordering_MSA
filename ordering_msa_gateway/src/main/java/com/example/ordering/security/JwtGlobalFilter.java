package com.example.ordering.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtGlobalFilter implements GlobalFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final List<String> allowUrl = Arrays.asList(
            "/member/create",
            "/doLogin",
            "/items",
            "/item/*/image");


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String reqUri = req.getURI().getPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        boolean isAllowed = allowUrl.stream().anyMatch(uri -> antPathMatcher.match(uri, reqUri));
        if (isAllowed) {
            return chain.filter(exchange);
       }

        String token = req.getHeaders().getFirst("Authorization");
        try {
            if (token != null) {
                if (!token.startsWith("Bearer "))
                    throw new IllegalArgumentException("The token is invalid or not present.");
                token = token.substring(7);

//                토큰 검증 및 claims 추출
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                req = exchange.getRequest().mutate()
                        .header("myEmail", email)
                        .header("myRole", role)
                        .build();

                exchange = exchange.mutate().request(req).build();
            } else {
                throw new RuntimeException("token is empty");
            }
        } catch (Exception e) {

            ServerHttpResponse res = exchange.getResponse();
            res.setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
