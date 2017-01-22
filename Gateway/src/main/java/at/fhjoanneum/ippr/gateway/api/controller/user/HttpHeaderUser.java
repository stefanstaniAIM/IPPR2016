package at.fhjoanneum.ippr.gateway.api.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import com.google.common.base.Preconditions;

import io.jsonwebtoken.Claims;

/**
 * Special object to cache user received by http request to forward them to the different services.
 */
public class HttpHeaderUser {

  private static final String CLAIM_ID = "claims";
  private static final String USER_ID = "userId";
  private static final String ROLES_ID = "roles";
  private static final String RULES_ID = "rules";

  private final String userId;
  private final String roles;
  private final String rules;

  public HttpHeaderUser(final HttpServletRequest request) {
    final Claims claims = (Claims) request.getAttribute(CLAIM_ID);
    Preconditions.checkNotNull(claims);
    Preconditions.checkNotNull(claims.get(USER_ID));
    Preconditions.checkNotNull(claims.get(ROLES_ID));
    Preconditions.checkNotNull(claims.get(RULES_ID));

    final String userId = String.valueOf(claims.get(USER_ID));
    final String roles = StringUtils.join((List<String>) claims.get(ROLES_ID), ",");
    final String rules = StringUtils.join((List<String>) claims.get(RULES_ID), ",");
    this.userId = userId;
    this.roles = roles;
    this.rules = rules;
  }

  public String getUserId() {
    return userId;
  }

  public String getRoles() {
    return roles;
  }

  public String getRules() {
    return rules;
  }

  public HttpHeaders getHttpHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(USER_ID, userId);
    headers.add(ROLES_ID, roles);
    headers.add(RULES_ID, rules);
    return headers;
  }
}
