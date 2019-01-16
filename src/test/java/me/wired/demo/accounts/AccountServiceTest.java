package me.wired.demo.accounts;

import me.wired.demo.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Test
    @TestDescription("User 이름 검색")
    public void findByUserName() {
        // Given
        String userName = "henry@gmail.com";
        String password = "12345";
        Set<AccountRole> roleSet = new HashSet<>();
        roleSet.add(AccountRole.ADMIN);
        roleSet.add(AccountRole.USER);

        Account account = Account.builder()
                .email(userName)
                .password(password)
                .roles(roleSet)
                .build();
        accountRepository.save(account);

        // When
        UserDetails userDetails = accountService.loadUserByUsername(userName);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

}