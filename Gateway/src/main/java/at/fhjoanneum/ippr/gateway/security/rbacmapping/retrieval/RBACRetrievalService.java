package at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval;

import java.util.Map;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;

public interface RBACRetrievalService {

  Map<String, CacheUser> getSystemUsers();
}
