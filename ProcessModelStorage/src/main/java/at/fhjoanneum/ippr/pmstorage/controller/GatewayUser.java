package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Special user in services to retrieve userId and groups of a user. Values are set in the http
 * header
 *
 */
public class GatewayUser {

  private static final String USER_ID = "userId";
  private static final String ROLES_ID = "roles";
  private static final String RULES_ID = "rules";

  private final Long userId;
  private List<String> roles = Lists.newArrayList();
  private List<String> rules = Lists.newArrayList();

  public GatewayUser(final HttpServletRequest request) {
    Preconditions.checkNotNull(request.getHeader(USER_ID));
    Preconditions.checkNotNull(request.getHeader(ROLES_ID));
    Preconditions.checkNotNull(request.getHeader(RULES_ID));

    this.userId = Long.valueOf(request.getHeader(USER_ID));
    this.roles =
        Splitter.on(",").omitEmptyStrings().trimResults().splitToList(request.getHeader(ROLES_ID));
    this.rules =
        Splitter.on(",").omitEmptyStrings().trimResults().splitToList(request.getHeader(RULES_ID));
  }

  public Long getUserId() {
    return userId;
  }

  public List<String> getRoles() {
    return ImmutableList.copyOf(roles);
  }

  public List<String> getRules() {
    return ImmutableList.copyOf(rules);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("userId", userId)
        .append("roles", roles).toString();
  }
}
