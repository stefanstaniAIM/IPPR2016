package at.fhjoanneum.ippr.gateway.security.authentication.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.gateway.security.authentication.AuthenticationService;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private AuthenticationService authenticationService;

  @RequestMapping(value = "login", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  public ResponseEntity<LoginResponse> login(@RequestBody final UserLogin login) {

    final Optional<User> userOpt =
        authenticationService.authenticateUser(login.name, login.password);

    if (!userOpt.isPresent()) {
      return new ResponseEntity<UserController.LoginResponse>(HttpStatus.UNAUTHORIZED);
    }

    final User user = userOpt.get();

    final LoginResponse loginResponse = new LoginResponse(Jwts.builder()
        .setSubject(user.getUsername()).claim("roles", user.getGroups()).setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)))
        .signWith(SignatureAlgorithm.HS256, "secretkey").compact());

    return new ResponseEntity<UserController.LoginResponse>(loginResponse, HttpStatus.OK);
  }


  private static class UserLogin implements Serializable {
    private static final long serialVersionUID = -431110191246364184L;

    public String name;
    public String password;
  }

  private static class LoginResponse implements Serializable {
    private static final long serialVersionUID = -431110191246364284L;

    private final String token;

    public LoginResponse(final String token) {
      this.token = token;
    }
  }
}
