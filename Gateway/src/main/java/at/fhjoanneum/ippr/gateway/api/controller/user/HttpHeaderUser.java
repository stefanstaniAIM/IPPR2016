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
  private static final String GROUP_ID = "groups";


  private final String userId;
  private final String groups;

  public HttpHeaderUser(final HttpServletRequest request) {
    final Claims claims = (Claims) request.getAttribute(CLAIM_ID);
    Preconditions.checkNotNull(claims);
    Preconditions.checkNotNull(claims.get(USER_ID));
    Preconditions.checkNotNull(claims.get(GROUP_ID));

    final String userId = String.valueOf(claims.get(USER_ID));
    final String groups = StringUtils.join((List<String>) claims.get(GROUP_ID), ",");
    this.userId = userId;
    this.groups = groups;
  }

  public String getUserId() {
    return userId;
  }

  public String getGroups() {
    return groups;
  }

  public HttpHeaders getHttpHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(USER_ID, userId);
    headers.add(GROUP_ID, groups);
    return headers;
  }
}
