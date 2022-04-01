package com.couponmania2.coupon_project.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.couponmania2.coupon_project.exceptions.AppUnauthorizedRequestException;
import com.couponmania2.coupon_project.exceptions.AppUnauthorizedRequestMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtils {
    //TODO: add a jwtGrantedAuthorityConverter
    private final String idClaimKey = "id";
    private final String roleClaimKey = "authorities";

    @Value("${app.auth.key}")
    private String authKey;

    public String generateToken(UserModel userDetails) {
        try {
            //TODO: change to application properties+ check if needs to be final
            Algorithm algorithmHS = Algorithm.HMAC256(authKey.getBytes());
            String token = JWT.create()
                    .withSubject(userDetails.getUsername())
                    //todo: create dateUtils
                    .withIssuedAt(java.sql.Date.valueOf(LocalDate.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60*30)))
                    .withClaim(idClaimKey, userDetails.getId())
                    .withClaim(roleClaimKey, userDetails.getAuthorities())
//                    .withClaim("authorities" ,
//                            authResult.getAuthorities()
//                                    .stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
//                                    .collect(Collectors.toList()))
                    .sign(algorithmHS);

            return token;

        } catch (JWTCreationException err) {
            //TODO: handle exception
            err.printStackTrace();
        }
        return null;
    }

//    public String generateToken(String userDetails) {
//        try {
//            //TODO: change to application properties+ check if needs to be final
//            Algorithm algorithmHS = Algorithm.HMAC256(.getBytes());
//            String token = JWT.create()
//                    .withSubject(userDetails.())
//                    //todo: create dateUtils
//                    .withIssuedAt(java.sql.Date.valueOf(LocalDate.now()))
//                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60*30)))
//                    .withClaim(idClaimKey, userDetails.getId())
//                    .withClaim(roleClaimKey, userDetails.getRole())
////                    .withClaim("authorities" ,
////                            authResult.getAuthorities()
////                                    .stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
////                                    .collect(Collectors.toList()))
//                    .sign(algorithmHS);
//
//            return token;
//
//        } catch (JWTCreationException err) {
//            //TODO: handle exception
//            err.printStackTrace();
//        }
//        return null;
//    }


    public UserModel validateToken(String token) throws AppUnauthorizedRequestException {
        try{
            DecodedJWT jwt = JWT.decode(token);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(authKey.getBytes()))
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt.getToken());
            UserModel user = new UserModel();
            user.setUsername(decodedJWT.getSubject());
            user.setId(decodedJWT.getClaim(idClaimKey).asLong());
            user.setAuthorities((List<GrantedAuthority>) decodedJWT.getClaim(roleClaimKey).as(ArrayList.class));
            return user;
        } catch (TokenExpiredException err){
            throw new AppUnauthorizedRequestException(AppUnauthorizedRequestMessage.LOGIN_EXPIRED.getMessage());
        }
        catch (Exception err){
            throw new AppUnauthorizedRequestException(AppUnauthorizedRequestMessage.NO_LOGIN.getMessage());
        }
    }
}
