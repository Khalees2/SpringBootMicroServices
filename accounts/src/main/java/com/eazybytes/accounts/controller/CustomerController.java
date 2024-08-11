package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "CRUD REST APIs for Customer Details Resource",
        description = "REST API in EazyBank to fetch Customer Details"
)
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number should have only numbers and must be 10 digits")
                                                                   String mobileNumber){
        CustomerDetailsDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDetailsDto);
    }
}
