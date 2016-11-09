package at.fhjoanneum.ippr.gateway.security.authentication.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.gateway.security.authentication.AuthenticationService;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.persistence.repository.UserGroupRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @RequestMapping(value = "user/login", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  public ResponseEntity<LoginResponse> login(@RequestBody final UserLogin login) {

    final Optional<User> userOpt =
        authenticationService.authenticateUser(login.username, login.password);

    if (!userOpt.isPresent()) {
      return new ResponseEntity<UserController.LoginResponse>(HttpStatus.UNAUTHORIZED);
    }

    final User user = userOpt.get();

    final LoginResponse loginResponse = new LoginResponse(Jwts.builder()
        .setSubject(user.getUsername()).claim("userId", user.getUId()).setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)))
        .signWith(SignatureAlgorithm.HS256, "secretkey").compact());

    return new ResponseEntity<UserController.LoginResponse>(loginResponse, HttpStatus.OK);
  }

  @RequestMapping(value = "api/me", method = {RequestMethod.GET},
      produces = "application/json; charset=UTF-8")
  public User login(final HttpServletRequest request) throws ServletException {
    final Claims claims = (Claims) request.getAttribute("claims");
    final Integer userId = (Integer) claims.get("userId");

    return userGroupRepository.getUserByUserId(userId.longValue()).get();
  }

  private static class UserLogin implements Serializable {
    private static final long serialVersionUID = -431110191246364184L;

    public String username;
    public String password;
  }

  private static class LoginResponse implements Serializable {
    private static final long serialVersionUID = -431110191246364284L;

    public final String token;

    public LoginResponse(final String token) {
      this.token = token;
    }
  }
}
