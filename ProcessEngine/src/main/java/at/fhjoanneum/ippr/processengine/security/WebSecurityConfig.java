package at.fhjoanneum.ippr.processengine.security;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin()
        .failureHandler((final HttpServletRequest request, final HttpServletResponse response,
            final AuthenticationException exception) -> {
          // Authentication failed, send error response.
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          final PrintWriter writer = response.getWriter();
          writer.println("HTTP Status 401 : " + exception.getMessage());
        });
    http.csrf().disable();
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    // auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups")
    // .contextSource().ldif("classpath:test-server.ldif");

    auth.inMemoryAuthentication().withUser("user").password("password").authorities("ROLE_USER");

  }
}
