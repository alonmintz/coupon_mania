package com.couponmania2.coupon_project.filters;
import com.couponmania2.coupon_project.auth.JwtUtils;
import com.couponmania2.coupon_project.auth.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = new JwtUtils();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attempt");
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        System.out.println("here");
        UserModel userModel = (UserModel) authResult.getPrincipal();
        jwtUtils.generateToken(userModel);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtUtils.generateToken(userModel));
        tokens.put("refresh_token", jwtUtils.generateRefreshToken(userModel));
        tokens.forEach((k,v)-> System.out.println(k + " " + v));
        response.setContentType("application/json");
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
