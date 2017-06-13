package at.fhjoanneum.ippr.processengine.akka.tasks;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;

@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public abstract class AbstractTask<I> extends UntypedActor implements Task<I> {

  private final static Logger LOG = LoggerFactory.getLogger(AbstractTask.class);

  private TaskCallback<?> callback;

  private UntypedActorContext parentContext;

  public AbstractTask() {}

  public <T> AbstractTask(final TaskCallback<T> callback) {
    checkNotNull(callback);
    this.callback = callback;
  }

  public AbstractTask(final UntypedActorContext parentContext) {
    checkNotNull(parentContext);
    this.parentContext = parentContext;
  }

  public <T> AbstractTask(final TaskCallback<T> callback, final UntypedActorContext parentContext) {
    checkNotNull(callback);
    checkNotNull(parentContext);
    this.callback = callback;
    this.parentContext = parentContext;
  }

  @Override
  public void onReceive(final Object obj) throws Exception {
    try {
      if (canHandle(obj)) {
        execute((I) obj);
      } else {
        LOG.warn("Cannot handle received message: {}", obj);
        unhandled(obj);
      }
    } finally {
      getContext().stop(getSelf());
    }
  }

  @Override
  public <T> void registerCallback(final TaskCallback<T> callback) {
    this.callback = callback;
  }

  protected <T> void callback(final T value) {
    ((TaskCallback<T>) callback).callback(value);
  }

  protected <T> void callback() {
    ((TaskCallback<T>) callback).callback(null);
  }

  protected UntypedActorContext getParentContext() {
    if (parentContext == null) {
      throw new UnsupportedOperationException("Context of parent is not set");
    }
    return parentContext;
  }
}
