package at.fhjoanneum.ippr.processengine.security;

// @Configuration
// @EnableWebSecurity
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {

  // @Override
  // protected void configure(final HttpSecurity http) throws Exception {
  // http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin()
  // .failureHandler((final HttpServletRequest request, final HttpServletResponse response,
  // final AuthenticationException exception) -> {
  // // Authentication failed, send error response.
  // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  // final PrintWriter writer = response.getWriter();
  // writer.println("HTTP Status 401 : " + exception.getMessage());
  // });
  // http.csrf().disable();
  // }
  //
  // @Override
  // public void configure(final WebSecurity web) throws Exception {
  // web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  // }
  //
  // @Autowired
  // public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
  // // auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups")
  // // .contextSource().ldif("classpath:test-server.ldif");
  //
  // auth.inMemoryAuthentication().withUser("user").password("password").authorities("ROLE_USER");
  //
  // }
}
