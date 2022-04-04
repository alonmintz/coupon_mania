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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUtils {
    //TODO: add a jwtGrantedAuthorityConverter
    private static final String idClaimKey = "id";
    private static final String roleClaimKey = "authorities";

    //    @Value("${app.auth.key}")
    //TODO: decrypt
    private static final String authKey = "secret";

    public static String generateToken(UserModel userDetails) {
        try {
            //TODO: change to application properties+ check if needs to be final
            Algorithm algorithmHS = Algorithm.HMAC256(authKey.getBytes());
            String token = JWT.create()
                    .withSubject(userDetails.getUsername())
                    //todo: create dateUtils
                    .withIssuedAt(java.sql.Date.valueOf(LocalDate.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60 * 30)))
                    .withClaim(idClaimKey, userDetails.getId())
                    .withClaim(roleClaimKey,
                            userDetails.getAuthorities().stream().map
                                    (GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithmHS);

            return token;

        } catch (JWTCreationException err) {
            //TODO: handle exception
            err.printStackTrace();
        }
        return null;
    }

    public static String generateRefreshToken(UserModel userDetails) {
        try {
            //TODO: change to application properties+ check if needs to be final
            Algorithm algorithmHS = Algorithm.HMAC256(authKey.getBytes());
            String token = JWT.create()
                    .withSubject(userDetails.getUsername())
                    //todo: create dateUtils
                    .withIssuedAt(java.sql.Date.valueOf(LocalDate.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60 * 30)))
                    .withClaim(idClaimKey, userDetails.getId())
                    .withClaim(roleClaimKey,
                            userDetails.getAuthorities().stream().map
                                    (GrantedAuthority::getAuthority).collect(Collectors.toList()))
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

    public static UserModel validateToken(String token) throws AppUnauthorizedRequestException {
        try {
            DecodedJWT jwt = JWT.decode(token);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(authKey.getBytes()))
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt.getToken());
            String username = decodedJWT.getSubject();
            long id = decodedJWT.getClaim(idClaimKey).asLong();
            List<String> authorities = decodedJWT.getClaim(roleClaimKey).asList(String.class);
            List<GrantedAuthority> grantedAuthorities = authorities
                    .stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
            return new UserModel(username, "SECRET", id, grantedAuthorities);
        } catch (TokenExpiredException err) {
            throw new AppUnauthorizedRequestException(AppUnauthorizedRequestMessage.LOGIN_EXPIRED.getMessage());
        } catch (Exception err) {
            throw new AppUnauthorizedRequestException(AppUnauthorizedRequestMessage.NO_LOGIN.getMessage());
        }
    }
}
