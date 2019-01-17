package me.wired.demo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties("my-app")
@Getter
@Setter
public class AppProperties {
    @NotEmpty
    String adminUsername;
    @NotEmpty
    String adminPassword;
    @NotEmpty
    String userUsername;
    @NotEmpty
    String userPassword;
    @NotEmpty
    String clientId;
    @NotEmpty
    String clientSecret;
}
