package at.fhjoanneum.ippr.communicator.composer.datatype;

public interface DataTypeComposer<T> {

  String compose(T input);
}
