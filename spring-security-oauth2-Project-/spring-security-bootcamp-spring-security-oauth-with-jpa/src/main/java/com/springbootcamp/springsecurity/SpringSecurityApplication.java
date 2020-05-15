package com.springbootcamp.springsecurity;

import com.springbootcamp.springsecurity.security.Bootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
@EnableMongoRepositories
public class SpringSecurityApplication {


	@Autowired
	private TokenStore tokenStore;

	@GetMapping("/doLogout")
	public String logout(HttpServletRequest request){
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
		}
		return "Logged out successfully";
	}


	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SpringSecurityApplication.class, args);

		Bootstrap init = applicationContext.getBean(Bootstrap.class);
		init.initialize();


	}

}
