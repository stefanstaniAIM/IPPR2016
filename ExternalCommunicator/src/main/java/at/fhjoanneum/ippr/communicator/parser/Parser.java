package at.fhjoanneum.ippr.communicator.parser;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public interface Parser<I> {

  InternalData parse(I input);
}
