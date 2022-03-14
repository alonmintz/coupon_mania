package com.couponmania2.coupon_project.beans;

import com.couponmania2.coupon_project.serialization.CompanyForm;
import com.couponmania2.coupon_project.serialization.CompanySerializer;
import com.couponmania2.coupon_project.serialization.CustomerSerializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Data
@Entity
@Table(name= "companies")
@JsonSerialize(using = CompanySerializer.class)
public class Company {


    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;

    @Column(nullable = false , length = 30)
    private String name;

    @Column(nullable = false , length = 40)
    private String email;

    @Column(nullable = false , length = 30)
    private String password;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY , mappedBy = "company" , orphanRemoval = true)
    private Set<Coupon> coupons = new HashSet<>();

    public Company(CompanyForm companyForm){
        this(companyForm.getName() , companyForm.getEmail() ,companyForm.getPassword(), new HashSet<>());
    }


    protected Company() {
    }

    public Company(String name, String email, String password, Set<Coupon> coupons) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public Company(String name, String email, String password) {
        this(name,email,password,new HashSet<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }
}
