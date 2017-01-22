package at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.memory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheRole;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheRule;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.RBACRetrievalService;
import au.com.bytecode.opencsv.CSVReader;

public class RBACRetrievalServiceMemoryImpl implements RBACRetrievalService {

  private static final Logger LOG = LoggerFactory.getLogger(RBACRetrievalServiceMemoryImpl.class);

  private static Map<String, CacheUser> users = null;

  @Override
  public Map<String, CacheUser> getSystemUsers() {
    if (users == null) {
      users = Maps.newHashMap();
      readCsv();
    }
    return users;
  }

  private void readCsv() {
    final Map<String, CacheRule> rules = readRules();
    final Map<String, CacheRole> roles = readRoles(rules);
    readUsers(roles);
  }

  private Map<String, CacheRule> readRules() {
    final Map<String, CacheRule> rules = Maps.newHashMap();

    final Reader in = null;
    CSVReader reader = null;

    try {
      final InputStream is = this.getClass().getResourceAsStream("/memoryusers/rules.csv");
      reader = new CSVReader(new InputStreamReader(is), '\n', '\'', 1);
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        final List<String> values =
            Splitter.on(';').omitEmptyStrings().trimResults().splitToList(nextLine[0]);
        final CacheRule rule =
            new CacheRule(values.get(RuleRow.SYSTEM_ID.index), values.get(RuleRow.NAME.index));
        rules.put(rule.getSystemId(), rule);
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    } finally {
      closeReader(in, reader);
    }

    return rules;
  }

  private Map<String, CacheRole> readRoles(final Map<String, CacheRule> rules) {
    final Map<String, CacheRole> roles = Maps.newHashMap();

    final Reader in = null;
    CSVReader reader = null;

    try {
      final InputStream is = this.getClass().getResourceAsStream("/memoryusers/roles.csv");
      reader = new CSVReader(new InputStreamReader(is), '\n', '\'', 1);
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        final List<String> values =
            Splitter.on(';').omitEmptyStrings().trimResults().splitToList(nextLine[0]);
        final String roleRulesRaw = values.get(RoleRow.RULES.index);
        final List<CacheRule> roleRules =
            Splitter.on(',').omitEmptyStrings().trimResults().splitToList(roleRulesRaw).stream()
                .map(systemId -> rules.get(systemId)).collect(Collectors.toList());
        final CacheRole role = new CacheRole(values.get(RoleRow.SYSTEM_ID.index),
            values.get(RoleRow.NAME.index), roleRules);
        roles.put(role.getSystemId(), role);
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    } finally {
      closeReader(in, reader);
    }
    return roles;
  }

  private void readUsers(final Map<String, CacheRole> roles) {
    final Reader in = null;
    CSVReader reader = null;

    try {
      final InputStream is = this.getClass().getResourceAsStream("/memoryusers/users.csv");
      reader = new CSVReader(new InputStreamReader(is), '\n', '\'', 1);
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        final List<String> values =
            Splitter.on(';').omitEmptyStrings().trimResults().splitToList(nextLine[0]);

        final String userRolesRaw = values.get(UserRow.ROLES.index);
        final List<CacheRole> userRoles =
            Splitter.on(',').omitEmptyStrings().trimResults().splitToList(userRolesRaw).stream()
                .map(role -> roles.get(role)).collect(Collectors.toList());

        final CacheUser cacheUser = new CacheUser(values.get(UserRow.SYSTEM_ID.index),
            values.get(UserRow.FIRST_NAME.index), values.get(UserRow.LAST_NAME.index),
            values.get(UserRow.USER_NAME.index), userRoles, values.get(UserRow.PASSWORD.index));
        users.put(cacheUser.getUsername(), cacheUser);
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    } finally {
      closeReader(in, reader);
    }
  }

  private void closeReader(final Reader in, final CSVReader reader) {
    try {
      if (reader != null) {
        reader.close();
      }
      if (in != null) {
        in.close();
      }
    } catch (final IOException e) {
      LOG.error(e.getMessage());
    }
  }

  private enum RuleRow {
    NAME(0), SYSTEM_ID(1);

    private final int index;

    private RuleRow(final int index) {
      this.index = index;
    }
  }

  private enum RoleRow {
    NAME(0), SYSTEM_ID(1), RULES(2);

    private final int index;

    private RoleRow(final int index) {
      this.index = index;
    }
  }

  private enum UserRow {
    USER_NAME(0), FIRST_NAME(1), LAST_NAME(2), PASSWORD(3), SYSTEM_ID(4), ROLES(5);

    private final int index;

    private UserRow(final int index) {
      this.index = index;
    }
  }
}
