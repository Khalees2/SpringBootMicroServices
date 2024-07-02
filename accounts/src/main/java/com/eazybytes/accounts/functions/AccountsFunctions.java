package com.eazybytes.accounts.functions;

import com.eazybytes.accounts.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {
    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Autowired
    private AccountsService accountsService;

    @Bean
    public Consumer<Long> updateCommunication(){
        return accountNumber ->{
            log.info("Communication successfully sent for the account number : "+accountNumber.toString());
            boolean updated = accountsService.updateCommunicationStatus(accountNumber);
            log.info("Is communication status updated in the database : "+updated);
        };
    }
}
