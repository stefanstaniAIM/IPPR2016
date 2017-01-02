package at.fhjoanneum.ippr.processengine.akka.tasks;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

@Transactional
public abstract class AbstractTask extends UntypedActor implements Task {

  private final static Logger LOG = LoggerFactory.getLogger(AbstractTask.class);

  private TaskCallback<?> callback;

  public AbstractTask() {}

  public <T> AbstractTask(final TaskCallback<T> callback) {
    this.callback = callback;
  }

  @Override
  public void onReceive(final Object obj) throws Exception {
    if (canHandle(obj)) {
      execute(obj);
    } else {
      LOG.warn("Cannot handle received message: {}", obj);
      unhandled(obj);
    }

    getContext().stop(getSelf());
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
}
