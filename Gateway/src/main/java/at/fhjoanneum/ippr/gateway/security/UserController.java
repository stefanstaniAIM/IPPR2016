package at.fhjoanneum.ippr.gateway.security;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
// @CrossOrigin(origins = "http://localhost:3000")
public class UserController {

  private final Map<String, List<String>> userDb = new HashMap<>();

  public UserController() {
    userDb.put("tom", Arrays.asList("user"));
    userDb.put("sally", Arrays.asList("user", "admin"));
  }

  @RequestMapping(value = "login", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  public ResponseEntity<LoginResponse> login(@RequestBody final UserLogin login) {
    if (login.name == null || !userDb.containsKey(login.name)) {
      return new ResponseEntity<UserController.LoginResponse>(HttpStatus.UNAUTHORIZED);
    }
    final LoginResponse loginResponse = new LoginResponse(
        Jwts.builder().setSubject(login.name).claim("roles", userDb.get(login.name))
            .setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1))).signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    return new ResponseEntity<UserController.LoginResponse>(loginResponse, HttpStatus.OK);
  }
  

  @SuppressWarnings("unused")
  private static class UserLogin implements Serializable {
    private static final long serialVersionUID = -431110191246364184L;

    public String name;
    public String password;
  }

  @SuppressWarnings("unused")
  private static class LoginResponse {
    public String token;

    public LoginResponse(final String token) {
      this.token = token;
    }
  }
}
