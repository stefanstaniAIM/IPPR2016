package at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.memory;

import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheGroup;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.UserGroupSystemRetrievalService;

public class UserGroupSystemRetrievalServiceMemoryImpl implements UserGroupSystemRetrievalService {

  private static Map<String, CacheUser> users;

  @Override
  public Map<String, CacheUser> getSystemUsers() {
    if (users == null) {
      createMemoryUsers();
    }
    return users;
  }

  private void createMemoryUsers() {
    final CacheGroup groupBoss = new CacheGroup("BOSS", "BOSS");
    final CacheGroup groupEmployee = new CacheGroup("EMPLOYEE", "EMPLOYEE");
    final CacheGroup groupAdmin = new CacheGroup("ADMIN", "ADMIN");

    final CacheUser userA = new CacheUser("111", "Stefan", "Stani",
        "stefan.stani@edu.fh-joanneum.at", Lists.newArrayList(groupEmployee, groupBoss), "hallo");
    final CacheUser userB = new CacheUser("222", "Matthias", "Geisriegler",
        "matthias.geisriegler@edu.fh-joanneum.at", Lists.newArrayList(groupEmployee, groupAdmin), "hallo");
    final CacheUser userC = new CacheUser("333", "Robert", "Singer",
        "robert.singer@edu.fh-joanneum.at", Lists.newArrayList(groupBoss, groupBoss, groupAdmin), "hallo");

    users = Maps.newHashMap();
    users.put(userA.getUsername(), userA);
    users.put(userB.getUsername(), userB);
    users.put(userC.getUsername(), userC);
  }
}
