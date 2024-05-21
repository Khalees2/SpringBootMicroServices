package com.eazybytes.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {
    @NotEmpty(message = "Name can not be null or empty")
    @Size(min=4,max = 30,message = "The length of the Customer Name should be between 4 and 30")
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email address is not valid")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number should have only numbers and must be 10 digits")
    private String mobileNumber;

    private AccountsDto accountsDto;
}
