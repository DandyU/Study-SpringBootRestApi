package me.wired.demo.accounts;

import me.wired.demo.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @TestDescription("User 이름 검색")
    public void findByUsername() {
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
        //accountRepository.save(account);
        // Set password encoding
        accountService.saveAccount(account);

        // When
        UserDetails userDetails = accountService.loadUserByUsername(userName);

        // Then
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    // # 1. 아래처럼 작성하면 하나의 Exception만 처리할 수 있음
    /*@Test(expected =  UsernameNotFoundException.class)
    @TestDescription("User 이름 검색")
    public void findByUsernameNotFound() {
        String username = "emptyUsername";
        accountService.loadUserByUsername(username);
    }*/

    // # 2. 아래와 같이하면 여러 Exception을 처리할 수 있음, 코드가 좀 복잡
   /*@Test
    @TestDescription("User 이름 검색")
    public void findByUsernameNotFound() {
        String username = "emptyUsername";
        try {
            accountService.loadUserByUsername(username);
            fail("Test is supposed to be failed.");
        } catch (UsernameNotFoundException e) {
            //assertThat(e instanceof UsernameNotFoundException).isTrue();
            assertThat(e.getMessage()).containsSequence(username);
        }
    }*/

    // # 3. ExpectedException 사용, 코드가 좀 간단해짐
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @TestDescription("User 이름 검색")
    public void findByUsernameNotFound() {
        String username = "emptyUsername";

        // Expected
        // expectedException is supposed to be here.
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // when
        accountService.loadUserByUsername(username);
    }
}