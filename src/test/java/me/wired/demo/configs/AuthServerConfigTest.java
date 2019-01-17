package me.wired.demo.configs;

import me.wired.demo.accounts.Account;
import me.wired.demo.accounts.AccountRole;
import me.wired.demo.accounts.AccountService;
import me.wired.demo.common.AppProperties;
import me.wired.demo.common.BaseControllerTest;
import me.wired.demo.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급받는 테스트")
    public void getAuthToken() throws Exception {
        // OAuth2의 Grant Type 중 Password, Refresh Token만 지원
        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();
        String username = appProperties.getUserUsername();
        String password = appProperties.getUserPassword();

        /*Account yuseon = Account.builder()
                .email(username)
                .password(password)
                .roles(new HashSet<>(Arrays.asList(AccountRole.ADMIN, AccountRole.USER)))
                .build();
        accountService.saveAccount(yuseon);*/

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists())
                ;
    }

}