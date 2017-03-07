package at.fhjoanneum.ippr.communicator.akka.config;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

public class SpringActorProducer implements IndirectActorProducer {

  private final ApplicationContext applicationContext;
  private final String actorBeanName;
  private Object[] args;

  public SpringActorProducer(final ApplicationContext applicationContext,
      final String actorBeanName) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
  }

  public SpringActorProducer(final ApplicationContext applicationContext,
      final String actorBeanName, final Object... args) {
    this(applicationContext, actorBeanName);
    this.args = args;
  }

  @Override
  public Actor produce() {
    if (args == null) {
      return (Actor) applicationContext.getBean(actorBeanName);
    } else {
      return (Actor) applicationContext.getBean(actorBeanName, args);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends Actor> actorClass() {
    return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
  }
}
