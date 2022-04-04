package com.couponmania2.coupon_project.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.couponmania2.coupon_project.auth.JwtUtils;
import com.couponmania2.coupon_project.auth.UserModel;
import com.couponmania2.coupon_project.exceptions.AppUnauthorizedRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class JwtTokenVerifier extends OncePerRequestFilter {




//    @SneakyThrows(AppUnauthorizedRequestException.class)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(request.getServletPath());
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/swagger-ui.html#/")) {
            filterChain.doFilter(request, response);
            return;
        }

        //TODO: SIT WITH HONG, FAILURE RETURNS LOGIN HTML


        String authHeader = request.getHeader("Authorization");
        if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            UserModel userModel = JwtUtils.validateToken(authHeader.substring("Bearer ".length()));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUsername(),
                    userModel.getPassword(),
                    userModel.getAuthorities()
//                    userModel.getId(),
//                    userModel.getPassword(),

            );
//            authenticationToken.
            authenticationToken.setDetails(userModel);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception err){
            System.out.println(err.getMessage());
            response.setHeader("error" , err.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", err.getMessage());
            response.setContentType("application/json");
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } catch (IOException e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        }
////        String myKey = "cupomaniac";
//
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            DecodedJWT decodedToken = JWT.decode(token);
//            String subject = decodedToken.getSubject();
//            List<SimpleGrantedAuthority> authorities = decodedToken.getClaim("authorities").asList(SimpleGrantedAuthority.class);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        } catch (JWTDecodeException err) {
//            err.printStackTrace();
//        }


    }
}
