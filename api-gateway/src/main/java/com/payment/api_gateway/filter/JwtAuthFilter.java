package com.payment.api_gateway.filter;


import com.payment.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.lang.annotation.Annotation;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/signup",
            "/auth/login"
    );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        String normalizedPath = path.replace("/+$", "");

        System.out.println("Incoming Path: " + path);
        System.out.println("Normalized Path: "+ normalizedPath);

        // Skip Jwt check for public path
        if (PUBLIC_PATHS.contains(normalizedPath)){
            System.out.println("Skip Jwt for Public path: "+ normalizedPath);
            return chain.filter(exchange)
                    .doOnSubscribe(s -> System.out.println("Proceeding without Jwt for public path"))
                    .doOnSuccess(v -> System.out.println("Successfully Passed public path without Jwt"))
                    .doOnError(e -> System.err.println("Error during public path filter chain" + e.getMessage()));
        }

        // Extract Authorization header once
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization header found: " + true);
        
        if (!authHeader .startsWith("Bearer ")){
            System.err.println("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String authtoken = authHeader.substring(7);
            System.out.println("Jwt token extracted from header: "+ authtoken);


            // DEBUG: replace <your-extracted-token-here> with the actual token for debugging only
            String expectedToken = "<your-extracted-token-here";
            System.out.println("Comparing Token: ");
            System.out.println("Expected Token: " + expectedToken);
            System.out.println("Extracted token: " + authtoken);


            Claims claims = JwtUtil.validateToken(authtoken);
            System.out.println("Jwt Validated successfully. Claims  subject: "+ claims.getSubject());

            // add user email to header
            exchange.getRequest().mutate()
                    .header("X-User-Email", claims.getSubject())
                    .build();

            System.out.println("Added X-User-Email header to request: "+claims.getSubject());

            return chain.filter(exchange)
                    .doOnSubscribe(s -> System.out.println("Proceeding with JWT authenticated request"))
                    .doOnSuccess(v -> System.out.println("Successfully passed JWT auth filter"))
                    .doOnError(e -> System.err.println("Error during authenticated filter chain: " + e.getMessage()));

        } catch (Exception e) {
            System.err.println("Jwt validation failed: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


    }

    @Override
    public int getOrder() {
        return 0;
    }
}
