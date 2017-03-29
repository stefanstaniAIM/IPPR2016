package at.fhjoanneum.ippr.communicator.parser;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class ParseResult {

  private final String transferId;
  private final InternalData data;

  public ParseResult(final String transferId, final InternalData data) {
    this.transferId = transferId;
    this.data = data;
  }

  public String getTransferId() {
    return transferId;
  }

  public InternalData getData() {
    return data;
  }
}
