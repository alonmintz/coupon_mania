package com.couponmania2.coupon_project.security;


import com.couponmania2.coupon_project.filters.JwtTokenVerifier;
import com.couponmania2.coupon_project.filters.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder PASSWORD_ENCODER;
    private final UserDetailsService USER_DETAILS_SERVICE;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(USER_DETAILS_SERVICE).passwordEncoder(PASSWORD_ENCODER);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("admin");
        http.authorizeRequests().anyRequest().authenticated();

        http
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()));
//                .addFilterBefore(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
//                .addFilterAfter(new JwtTokenVerifier(), JwtUsernameAndPasswordAuthenticationFilter.class);

//        http.authorizeRequests()
//                .antMatchers("/", "index", "/css", "/js", "media", "img", "/login/**")
//                .permitAll()
////                .antMatchers("/v2/api-docs",
////                        "/configuration/ui",
////                        "/swagger-resources/**",
////                        "/configuration/security",
////                        "/swagger-ui.html",
////                        "/webjars/**").permitAll()
//////                .antMatchers("/token/getToken").permitAll()
////                .antMatchers("/login/**").permitAll()
////                .antMatchers("/admin/**").hasRole("admin")
////                .antMatchers("/company/**").hasRole("company")
////                .antMatchers("/customer/**").hasRole("customer")
//                .anyRequest().authenticated();
//        http.authorizeRequests().antMatchers("/login").permitAll();


//
        http
                .formLogin().defaultSuccessUrl("http://localhost:8080/swagger-ui.html#/");


//        System.out.println("HERHEERHERHERH");

    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

}
