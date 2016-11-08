package at.fhjoanneum.ippr.gateway.security.usermapping.retrieval;

import org.springframework.context.annotation.Condition;

public interface UserGroupServiceCondition extends Condition {

  final static String PROPERTY_NAME = "usergroup.system.service";

}
