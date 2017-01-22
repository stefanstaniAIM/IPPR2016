package at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval;

import org.springframework.context.annotation.Condition;

public interface RBACServiceCondition extends Condition {

  final static String PROPERTY_NAME = "rbac.system.service";

}
