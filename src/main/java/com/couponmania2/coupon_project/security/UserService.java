package com.couponmania2.coupon_project.security;

import com.couponmania2.coupon_project.beans.Company;
import com.couponmania2.coupon_project.beans.Customer;
import com.couponmania2.coupon_project.repositories.CompanyRepo;
import com.couponmania2.coupon_project.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final CompanyRepo companyRepo;
    private final CustomerRepo customerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s.equals("admin@admin.com")){
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
            System.out.println(passwordEncoder.getClass());
//            return new User("admin@admin.com", passwordEncoder.encode("admin"), grantedAuthorities);
            return User.withUsername("admin@admin.com")
                    .password(passwordEncoder.encode("admin"))
                    .authorities(grantedAuthorities)
                    .build();

        }
        Optional<Company> companyOptional = companyRepo.findByEmail(s);
        if (companyOptional.isPresent()){
            Company company = companyOptional.get();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("company"));
            System.out.println("succefull login");
            return new User(company.getEmail(), company.getPassword(), grantedAuthorities);
        }
        Optional<Customer> customerOptional = customerRepo.findByEmail(s);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("customer"));
            return new User(customer.getEmail(), customer.getPassword(), grantedAuthorities);
        }
        //TODO: handle exception?
        throw new UsernameNotFoundException("Username not found.");
//        return null;
    }
}
