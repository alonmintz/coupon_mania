package com.couponmania2.coupon_project.facade;

import com.couponmania2.coupon_project.auth.ClientType;
import com.couponmania2.coupon_project.beans.Category;
import com.couponmania2.coupon_project.beans.Company;
import com.couponmania2.coupon_project.beans.Coupon;
import com.couponmania2.coupon_project.exceptions.*;
import com.couponmania2.coupon_project.repositories.CompanyRepo;
import com.couponmania2.coupon_project.repositories.CouponRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepo companyRepo;
    private final CouponRepo couponRepo;

    @Override
    public Company checkCredentials(String email, String password, ClientType clientType) throws AppUnauthorizedRequestException {

        Optional<Company> companyOptional = companyRepo.findByEmailAndPassword(email, password);
        if (companyOptional.isEmpty() || !clientType.equals(ClientType.company)) {
            throw new AppUnauthorizedRequestException(AppUnauthorizedRequestMessage.BAD_CREDENTIALS.getMessage());
        }
        return companyOptional.get();
    }

    @Override
    public void addCoupon(Coupon coupon) throws AppTargetExistsException {
        if (couponRepo.existsByCompanyAndTitle(coupon.getCompany(), coupon.getTitle())) {
            throw new AppTargetExistsException(AppTargetExistsMessage.COUPON_EXISTS);
        }
        couponRepo.save(coupon);
    }


    @Override
    public void updateCoupon(Coupon coupon) throws AppInvalidInputException {
        if (couponRepo.getById(coupon.getId()).getCompany() != coupon.getCompany()) {
            throw new AppInvalidInputException(AppInvalidInputMessage.UNMATCHING_COUPON);
        }
        couponRepo.save(coupon);
    }

    @Override
    public void deleteCoupon(long couponId, long companyId) throws AppTargetNotFoundException, AppInvalidInputException {
        Optional<Coupon> couponOptional = couponRepo.findById(couponId);
        if (couponOptional.isEmpty()) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COUPON_NOT_FOUND);
        }
        Coupon coupon = couponOptional.get();
        if (coupon.getCompany().getId() != companyId) {
            throw new AppInvalidInputException(AppInvalidInputMessage.UNMATCHING_COUPON);
        }

        couponRepo.deleteById(couponId);
    }

    @Override
    public Coupon getCouponByID(long couponId) throws AppTargetNotFoundException {
        if (!couponRepo.existsById(couponId)){
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COUPON_NOT_FOUND);
        }
        Optional<Coupon> optionalCoupon = couponRepo.findById(couponId);
        return optionalCoupon.get();
    }

    @Override
    public Set<Coupon> getAllCompanyCoupons(long companyId) throws AppTargetNotFoundException {
        if (!companyRepo.existsById(companyId)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        return couponRepo.findByCompany(companyRepo.getById(companyId));
    }

    @Override
    public Set<Coupon> getCompanyCouponsByCategory(long companyId, Category category) throws AppTargetNotFoundException {
        if (!companyRepo.existsById(companyId)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        return couponRepo.findByCompanyAndCategory(companyRepo.getById(companyId), category);
    }

    @Override
    public Set<Coupon> getCompanyCouponsByMaxPrice(long companyId, double maxPrice) throws AppTargetNotFoundException, AppInvalidInputException {
        if (!companyRepo.existsById(companyId)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        if (maxPrice <= 0) {
            throw new AppInvalidInputException(AppInvalidInputMessage.NEGATIVE_PRICE);
        }
        return couponRepo.findByCompanyAndPriceLessThanEqual(companyRepo.getById(companyId), maxPrice);
    }

    @Override
    public Company getCompanyDetails(long companyId) throws AppTargetNotFoundException {
        if (!companyRepo.existsById(companyId)) {
            throw new AppTargetNotFoundException(AppTargetNotFoundMessage.COMPANY_NOT_FOUND);
        }
        return companyRepo.getById(companyId);
    }
}
