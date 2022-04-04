package com.couponmania2.coupon_project.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@AllArgsConstructor
//@Data
//@NoArgsConstructor
////@Builder
//@Data
public class UserModel extends User {
    private final long id;
    private final ClientType clientType;

    public UserModel(String username, String password ,long id , ClientType clientType,Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.clientType = clientType;

    }
    //TODO: remove clienttype
    public UserModel(String username, String password ,long id , Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.clientType = ClientType.UNDEFINED;

    }

//    public UserModel(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//    }
//
//    public UserModel(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//    }

    public long getId() {
        return id;
    }

    //    public UserModel(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//    }
//    private long id = -1;
//    private String username;
//    private String password;
//    private List<GrantedAuthority> authorities = new ArrayList<>();
//
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return null;
////    }
////
////    @Override
////    public String getPassword() {
////        return null;
////    }
////
////    @Override
////    public String getUsername() {
////        return userName;
////    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//
//    @Override
//    public String toString() {
//        return "UserModel{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", authorities=" + authorities +
//                '}';
    }

