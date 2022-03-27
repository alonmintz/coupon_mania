package com.couponmania2.coupon_project.clr;

import com.couponmania2.coupon_project.auth.ClientType;
import com.couponmania2.coupon_project.beans.Customer;
import com.couponmania2.coupon_project.controller.ClientController;
import com.couponmania2.coupon_project.serialization.CustomerForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Order(2)
@RequiredArgsConstructor
public class AdminRestTest implements CommandLineRunner {


    private final RestTemplate restTemplate;

    private final String LOGIN_URL = "http://localhost:8080/admin/login?clientType={clientType}&userName={userName}&userPass={userPass}";
    private final String ADD_COMPANY_URL = "http://localhost:8080/admin/addCompany";
    private final String DELETE_COMPANY_URL = "http://localhost:8080/admin/deleteCompany/{id}";
    private final String UPDATE_COMPANY_URL = "http://localhost:8080/admin/updateCompany?id={id}";
    private final String GET_ALL_COMPANIES_URL = "http://localhost:8080/admin/getAllCompanies";
    private final String GET_ONE_COMPANY_URL = "http://localhost:8080/admin/getOneCompany/{id}";
    private final String ADD_CUSTOMER_URL = "http://localhost:8080/admin/addCustomer";
    private final String DELETE_CUSTOMER_URL = "http://localhost:8080/admin/deleteCustomer/{id}";
    private final String UPDATE_CUSTOMER_URL = "http://localhost:8080/admin/updateCustomer?id={id}";
    private final String GET_ALL_CUSTOMER_URL = "http://localhost:8080/admin/getAllCustomers";
    private final String GET_ONE_CUSTOMER_URL = "http://localhost:8080/admin/getOneCustomer/{id}";

    private HttpEntity<?> httpEntity;

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "admin@admin.com");
        params.put("userPass", "admin");
        params.put("clientType", ClientType.ADMIN);

//        ResponseEntity<?> token = restTemplate.postForEntity(LOGIN_URL , null , String.class , params);
        String token = restTemplate.postForObject(LOGIN_URL, HttpMethod.POST, String.class, params);
//        Map<String , String>
//        HttpEntity headerEntity = new HttpEntity(new HttpHeaders().set("Authorization" , token))
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        this.httpEntity = new HttpEntity<>(headers);
        System.out.println(token);

//        System.out.println(
//                restTemplate.exchange()
//        );

        getOneCustomer(1L);
        //getAllCustomers();

        CustomerForm cFORM = new CustomerForm();
        cFORM.setFirstName("alon2");
        cFORM.setLastName("mintz2");
        cFORM.setEmail("alon222mintz222@mintz");
        cFORM.setPassword("ggggggggg");
        addCustomer(cFORM , token);



        deleteCustomer(4L);


        CustomerForm cFORM2 = new CustomerForm();
        cFORM2.setFirstName("notAlon");
        cFORM2.setLastName("notMintz");
        //cFORM2.setEmail("alon222mintz222@mintz");
        cFORM2.setPassword("ggggggggg");
       // addCustomer(cFORM2 , token);
        updateCustomer(cFORM2 , 2L , token);
    }

    private void getOneCustomer(Long id) {
        //TODO: CHANGE URLS TO URI'S
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);

        Optional<Customer> c = restTemplate.exchange(GET_ONE_CUSTOMER_URL,
                HttpMethod.GET, httpEntity, Optional.class, params).getBody();

        System.out.println(c.get());

    }
    private void getAllCustomers() {
        //TODO: CHANGE URLS TO URI'S
        Map<String, Long> params = new HashMap<>();

        Set<Customer> c = restTemplate.exchange(GET_ALL_CUSTOMER_URL,
                HttpMethod.GET, httpEntity, Set.class).getBody();
        System.out.println(c);
    }

    private void addCustomer(CustomerForm customer , String token){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        this.httpEntity = new HttpEntity<>( customer, headers);

        restTemplate.exchange(ADD_CUSTOMER_URL , HttpMethod.POST , httpEntity , Void.class
                , new HashMap<>()
                );
    }

    private void deleteCustomer(Long id){
        Map<String , Long> params = new HashMap<>();
        params.put("id" , id);

        restTemplate.exchange(DELETE_CUSTOMER_URL , HttpMethod.DELETE,
                httpEntity , Void.class , params);
    }

    private void updateCustomer(CustomerForm customerForm , Long id , String token){
        Map<String , Long> params = new HashMap<>();
        params.put("id" , id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        this.httpEntity = new HttpEntity<>( customerForm, headers);

        restTemplate.exchange(UPDATE_CUSTOMER_URL , HttpMethod.PUT,
                httpEntity, Void.class , params);

    }
}