package com.couponmania2.coupon_project.facade;

import com.couponmania2.coupon_project.beans.Company;
import com.couponmania2.coupon_project.beans.Customer;
import com.couponmania2.coupon_project.repositories.CompanyRepo;
import com.couponmania2.coupon_project.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class AdminServiceImpl implements AdminService{
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    CustomerRepo customerRepo;

    @Override
    public void addCompany(Company company) throws Exception {
        if (companyRepo.existsByEmailOrName(company.getEmail() , company.getName())){
            throw new Exception();
        }
        companyRepo.save(company);
    }

    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public void updateCompany(Company company) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public void deleteCompany(int companyID) {

    }

    @Override
    public void deleteCustomer(int customerID) {

    }

    @Override
    public Set<Company> getAllComapnies() {
        return null;
    }

    @Override
    public Set<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public Company getOneCompany(int companyID) {
        return null;
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        return null;
    }
}
