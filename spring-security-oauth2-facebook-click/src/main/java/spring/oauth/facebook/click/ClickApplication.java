package spring.oauth.facebook.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
@EnableOAuth2Sso
public class ClickApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ClickApplication.class, args);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
				.authorizeRequests()
				.antMatchers("/", "/login**", "/error**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.logout()
				.logoutSuccessUrl("/")
				.permitAll()
				.and()
				.csrf();
				//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
}
