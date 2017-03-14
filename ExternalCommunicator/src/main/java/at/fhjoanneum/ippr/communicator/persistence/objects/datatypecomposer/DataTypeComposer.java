package at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public interface DataTypeComposer {

  Long getId();

  DataType getDataType();

  String getComposerClass();

  String getDescription();
}
