package me.wired.demo.configs;

import me.wired.demo.accounts.Account;
import me.wired.demo.accounts.AccountRole;
import me.wired.demo.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
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

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set<AccountRole> roleSet = new HashSet<>();
                roleSet.add(AccountRole.ADMIN);
                roleSet.add(AccountRole.USER);

                Account yuseon = Account.builder()
                        .email("yuseon@email.com")
                        .password("yuseon")
                        .roles(roleSet)
                        .build();
                accountService.saveAccount(yuseon);
            }

        };
    }
}
