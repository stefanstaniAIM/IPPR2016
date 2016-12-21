package at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.memory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheGroup;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.UserGroupSystemRetrievalService;
import au.com.bytecode.opencsv.CSVReader;

public class UserGroupSystemRetrievalServiceMemoryImpl implements UserGroupSystemRetrievalService {

  private static final Logger LOG =
      LoggerFactory.getLogger(UserGroupSystemRetrievalServiceMemoryImpl.class);

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
    final Map<String, CacheGroup> groups = readGroups();
    readUsers(groups);
  }

  private Map<String, CacheGroup> readGroups() {
    final Map<String, CacheGroup> groups = Maps.newHashMap();

    Reader in = null;
    CSVReader reader = null;

    try {
      InputStream is = this.getClass().getResourceAsStream("/memoryusers/groups.csv");
      reader = new CSVReader(new InputStreamReader(is), '\n', '\'', 1);
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        final List<String> values =
            Splitter.on(';').omitEmptyStrings().trimResults().splitToList(nextLine[0]);
        final CacheGroup group =
            new CacheGroup(values.get(GroupRow.SYSTEM_ID.index), values.get(GroupRow.NAME.index));
        groups.put(group.getSystemId(), group);
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    } finally {
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
    return groups;
  }

  private void readUsers(final Map<String, CacheGroup> groups) {
    Reader in = null;
    CSVReader reader = null;

    try {
      InputStream is = this.getClass().getResourceAsStream("/memoryusers/users.csv");
      reader = new CSVReader(new InputStreamReader(is), '\n', '\'', 1);
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        final List<String> values =
            Splitter.on(';').omitEmptyStrings().trimResults().splitToList(nextLine[0]);

        final String groupValues = values.get(UserRow.GROUPS.index);
        final List<CacheGroup> userGroups = Splitter.on(',').omitEmptyStrings().trimResults()
            .splitToList(groupValues).stream().map(group -> {
              final CacheGroup cacheGroup = groups.get(group);
              if (cacheGroup == null) {
                throw new IllegalStateException("Group definition is missing");
              }
              return cacheGroup;
            }).collect(Collectors.toList());

        final CacheUser cacheUser = new CacheUser(values.get(UserRow.SYSTEM_ID.index),
            values.get(UserRow.FIRST_NAME.index), values.get(UserRow.LAST_NAME.index),
            values.get(UserRow.USER_NAME.index), userGroups, values.get(UserRow.PASSWORD.index));
        users.put(cacheUser.getUsername(), cacheUser);
      }
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    } finally {
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
  }


  private enum GroupRow {
    NAME(0), SYSTEM_ID(1);

    private final int index;

    private GroupRow(final int index) {
      this.index = index;
    }
  }

  private enum UserRow {
    USER_NAME(0), FIRST_NAME(1), LAST_NAME(2), PASSWORD(3), SYSTEM_ID(4), GROUPS(5);

    private final int index;

    private UserRow(final int index) {
      this.index = index;
    }
  }
}
