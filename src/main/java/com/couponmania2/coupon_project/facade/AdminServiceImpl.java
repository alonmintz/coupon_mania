package com.couponmania2.coupon_project.facade;

import com.couponmania2.coupon_project.beans.Company;
import com.couponmania2.coupon_project.beans.Customer;
import com.couponmania2.coupon_project.exceptions.AppTargetExistsException;
import com.couponmania2.coupon_project.exceptions.AppTargetExistsMessage;
import com.couponmania2.coupon_project.exceptions.AppTargetNotFoundException;
import com.couponmania2.coupon_project.exceptions.AppTargetNotFoundMessage;
import com.couponmania2.coupon_project.repositories.CompanyRepo;
import com.couponmania2.coupon_project.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    CustomerRepo customerRepo;

    //todo: check if update checks can be handled in restcontroller

    @Override
    public void addCompany(Company company) throws AppTargetExistsException {
        if (companyRepo.existsByEmailOrName(company.getEmail(), company.getName())) {
            throw new AppTargetExistsException(AppTargetExistsMessage.COMPANY_EXISTS);
        }
        companyRepo.save(company);
    }

    @Override
    public void addCustomer(Customer customer) throws AppTargetExistsException {
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new AppTargetExistsException(AppTargetExistsMessage.CUSTOMER_EXISTS);
        }
        customerRepo.save(customer);
    }

    @Override
    public void updateCompany(Company company) throws AppTargetNotFoundException {
//        companyRepo.existsByIdAndName(company)
        if (!companyRepo.existsByIdAndName(company.getId(), company.getName())) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        companyRepo.save(company);
    }

    @Override
    public void updateCustomer(Customer customer) throws AppTargetNotFoundException {
        if (!customerRepo.existsById(customer.getId())) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.CUSTOMER_NOT_FOUND);
        }
        customerRepo.save(customer);
    }

    @Override
    public void deleteCompany(int companyID) throws AppTargetNotFoundException {
        if (!companyRepo.existsById(companyID)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        companyRepo.deleteById(companyID);
    }

    @Override
    public void deleteCustomer(int customerID) throws AppTargetNotFoundException {
        if (!customerRepo.existsById(customerID)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.CUSTOMER_NOT_FOUND);
        }
        customerRepo.deleteById(customerID);
    }

    @Override
    public Set<Company> getAllComapnies() {
        return new HashSet<>(companyRepo.findAll());
    }

    @Override
    public Set<Customer> getAllCustomers() {
        return new HashSet<>(customerRepo.findAll());
    }

    @Override
    public Company getOneCompany(int companyID) throws AppTargetNotFoundException {
        Optional<Company> companyOptional = companyRepo.findById(companyID);
        if (companyOptional.isEmpty()) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        return companyOptional.get();
    }

    @Override
    public Customer getOneCustomer(int customerID) throws AppTargetNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findById(customerID);
        if (customerOptional.isEmpty()) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.CUSTOMER_NOT_FOUND);
        }
        return customerOptional.get();
    }
}
