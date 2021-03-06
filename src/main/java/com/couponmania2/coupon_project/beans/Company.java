package com.couponmania2.coupon_project.beans;

import com.couponmania2.coupon_project.serialization.CompanyForm;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 30)
    private String password;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "company", orphanRemoval = true)
    private Set<Coupon> coupons = new HashSet<>();

    /**
     * C'tor for companyForm
     *
     * @param companyForm company form
     */
    public Company(CompanyForm companyForm) {
        this(companyForm.getName(), companyForm.getEmail(), companyForm.getPassword(), new HashSet<>());
    }
    /**
     * Private c'tor to disable instantiating a company with no data.
     */
    protected Company() {
    }

    /**
     * C'tor for company
     *
     * @param name     Company name
     * @param email    Company email
     * @param password Company password
     * @param coupons  Company coupons
     */
    public Company(String name, String email, String password, Set<Coupon> coupons) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    /**
     * C'tor company without coupons
     *
     * @param name     Company name
     * @param email    Company email
     * @param password Company email
     */
    public Company(String name, String email, String password) {
        this(name, email, password, new HashSet<>());
    }

    /**
     * Getter for the id
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for Company name
     *
     * @return Company name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Company name
     *
     * @param name Company name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter email company
     *
     * @return email Company
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter company email
     *
     * @param email Company email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter company password
     *
     * @return Company password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for company password
     *
     * @param password Company password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for Company coupons
     *
     * @return Company coupons set
     */
    public Set<Coupon> getCoupons() {
        return coupons;
    }

    /**
     * Setter company coupons
     *
     * @param coupons set company coupons
     */
    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }
}
