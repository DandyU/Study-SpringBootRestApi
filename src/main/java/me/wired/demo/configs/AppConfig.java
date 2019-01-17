package me.wired.demo.configs;

import me.wired.demo.accounts.Account;
import me.wired.demo.accounts.AccountRepository;
import me.wired.demo.accounts.AccountRole;
import me.wired.demo.accounts.AccountService;
import me.wired.demo.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // APP 구동 시에 처리할 작업 설정
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AccountRepository accountRepository;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account account;
                String email = appProperties.getAdminUsername();
                if (!accountRepository.findByEmail(email).isPresent()) {
                    account = Account.builder()
                            .email(email)
                            .password(appProperties.getAdminPassword())
                            .roles(new HashSet<>(Arrays.asList(AccountRole.ADMIN)))
                            .build();
                    accountService.saveAccount(account);
                }

                email = appProperties.getUserUsername();
                if (!accountRepository.findByEmail(email).isPresent()) {
                    account = Account.builder()
                            .email(email)
                            .password(appProperties.getUserPassword())
                            .roles(new HashSet<>(Arrays.asList(AccountRole.USER)))
                            .build();
                    accountService.saveAccount(account);
                }
            }
        };
    }
}
