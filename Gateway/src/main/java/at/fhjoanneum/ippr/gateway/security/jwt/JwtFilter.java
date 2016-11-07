package at.fhjoanneum.ippr.gateway.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import at.fhjoanneum.ippr.gateway.security.utils.ExceptionUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtFilter extends GenericFilterBean {

  @Override
  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      ExceptionUtils.createUnauthorizedException("Missing or invalid Authorization header.", res);
      return;
    }

    try {
      final String token = authHeader.substring(7); // The part after "Bearer "
      final Claims claims =
          Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
      request.setAttribute("claims", claims);
    } catch (final Exception e) {
      ExceptionUtils.createUnauthorizedException("Invalid token", res);
      return;
    }

    chain.doFilter(req, res);
  }

}
