package at.fhjoanneum.ippr.pmstorage.parser;

public interface FileParser<I, O> {

  public O parseFile(I input, String version);
}
