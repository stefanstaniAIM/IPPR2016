package at.fhjoanneum.ippr.gateway.security.usermapping.retrieval;

import java.util.Map;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;

public interface UserGroupSystemRetrievalService {

  Map<String, CacheUser> getSystemUsers();
}
