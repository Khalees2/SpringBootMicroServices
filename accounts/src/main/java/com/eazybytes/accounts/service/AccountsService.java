package com.eazybytes.accounts.service;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.AccountsMsgDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsService {

    private  static final Logger log = LoggerFactory.getLogger(AccountsService.class);

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /*@Autowired
    private final StreamBridge streamBridge;*/

    public void createAccount(CustomerDto customerDto){
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistException("Customer already registered with the given mobile number");
        }else{
            Customer savedCustomer = customerRepository.save(customer);
            Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
            //sendCommunication(savedAccount,savedCustomer);
        }
    }

    public CustomerDto getAccountByMobileNumber(String mobileNumber){
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()-> new ResourceNotFoundException("Customer", "Mobile Number",mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(()-> new ResourceNotFoundException("Account","Customer Id",customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));
        return customerDto;
    }

    public boolean updateAccount(CustomerDto customerDto){
        boolean isUpdated=false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(()->
                    new ResourceNotFoundException("Account","accountNumber",accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto,accounts);
            accountsRepository.save(accounts);

            Customer customer = customerRepository.findById(accounts.getCustomerId()).orElseThrow(()->
                    new ResourceNotFoundException("Customer","Customer ID",accounts.getCustomerId().toString()));
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated=true;
        }
        return isUpdated;
    }

    @Transactional
    public boolean deleteAccountByMobileNumber(String mobileNumber){
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
                new ResourceNotFoundException("Customer","Mobile Number",mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    @Transactional
    public boolean updateCommunicationStatus(Long accountNumber){
        boolean isUpdated = false;
        if(accountNumber != null){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    ()-> new ResourceNotFoundException("Account", "Account Number", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }

    private Accounts createNewAccount(Customer customer){
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomAccNum = 100000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccNum);
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);
        return accounts;
    }

    /*private void sendCommunication(Accounts account, Customer customer){
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(),customer.getName(),customer.getEmail(),customer.getMobileNumber());
        log.info("Sending Communication request for :"+accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0",accountsMsgDto);
        log.info("Is communication publishing request successful : "+result);
    }*/
}
