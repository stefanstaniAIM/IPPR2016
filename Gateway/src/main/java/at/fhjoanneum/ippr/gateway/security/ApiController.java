package at.fhjoanneum.ippr.gateway.security;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")
public class ApiController {

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "role/{role}", method = {RequestMethod.GET, RequestMethod.OPTIONS})
  public Boolean login(@PathVariable final String role, final HttpServletRequest request)
      throws ServletException {
    final Claims claims = (Claims) request.getAttribute("claims");

    return ((List<String>) claims.get("roles")).contains(role);
  }
  
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "roles", method = {RequestMethod.GET, RequestMethod.OPTIONS})
  public List<String> login(final HttpServletRequest request)
      throws ServletException {
    final Claims claims = (Claims) request.getAttribute("claims");

    return (List<String>) claims.get("roles");
  }
}
