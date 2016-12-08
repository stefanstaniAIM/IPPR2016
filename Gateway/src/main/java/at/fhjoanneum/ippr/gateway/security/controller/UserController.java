package at.fhjoanneum.ippr.gateway.security.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.user.UserDTO;
import at.fhjoanneum.ippr.gateway.security.authentication.AuthenticationService;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.services.UserGroupService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class UserController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private UserGroupService userGroupService;

  @RequestMapping(value = "user/login", method = RequestMethod.POST)
  public ResponseEntity<LoginResponse> login(@RequestBody final UserLogin login) {

    final Optional<User> userOpt =
        authenticationService.authenticateUser(login.username, login.password);

    if (!userOpt.isPresent()) {
      return new ResponseEntity<UserController.LoginResponse>(HttpStatus.UNAUTHORIZED);
    }

    final User user = userOpt.get();

    final List<String> groups =
        user.getGroups().stream().map(Group::getName).collect(Collectors.toList());

    final LoginResponse loginResponse = new LoginResponse(Jwts.builder()
        .setSubject(user.getUsername()).claim("userId", user.getUId()).claim("groups", groups)
        .setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)))
        .signWith(SignatureAlgorithm.HS256, "secretkey").compact());

    return new ResponseEntity<UserController.LoginResponse>(loginResponse, HttpStatus.OK);
  }

  @RequestMapping(value = "api/me", method = {RequestMethod.GET})
  public User login(final HttpServletRequest request) throws ServletException {
    final Claims claims = (Claims) request.getAttribute("claims");
    final Integer userId = (Integer) claims.get("userId");

    return userGroupService.getUserByUserId(userId.longValue());
  }

  @RequestMapping(value = "api/user/{userId}", method = RequestMethod.GET)
  public User getUser(final HttpServletRequest request,
      @PathVariable(name = "userId", required = true) final Long userId) {
    return userGroupService.getUserByUserId(userId);
  }

  @RequestMapping(value = "api/processes/possibleUsers/{group}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<UserDTO>> getPossibleUsers(
      @PathVariable("group") final String group) {

    return () -> {
      return userGroupService.getPossibleUsersOfGroup(group).get();
    };
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
