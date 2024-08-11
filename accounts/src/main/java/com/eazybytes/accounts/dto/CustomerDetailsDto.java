package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer Details",
        description = "This Schema hold Customer, Accounts, Loans and Cards information"
)
public class CustomerDetailsDto {
    @Schema(
            description = "Name of the customer",
            example = "Salman Khaleel"
    )
    @NotEmpty(message = "Name can not be null or empty")
    @Size(min=4,max = 30,message = "The length of the Customer Name should be between 4 and 30")
    private String name;

    @Schema(
            description = "Email of the customer",
            example = "Salmank640@gmail.com"
    )
    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email address is not valid")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile Number should have only numbers and must be 10 digits")
    private String mobileNumber;

    private AccountsDto accountsDto;

    private LoansDto loansDto;

    private CardsDto cardsDto;
}
