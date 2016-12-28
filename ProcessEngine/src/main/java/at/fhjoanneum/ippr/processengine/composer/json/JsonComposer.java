package at.fhjoanneum.ippr.processengine.composer.json;

public interface JsonComposer<T> {

  String compose(T value);
}
