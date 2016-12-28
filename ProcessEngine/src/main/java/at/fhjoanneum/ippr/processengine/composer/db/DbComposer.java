package at.fhjoanneum.ippr.processengine.composer.db;

public interface DbComposer<T> {

  T compose(String value);

  boolean canCompose(String value);
}
